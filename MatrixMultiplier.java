import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * MatrixMultiplier provides multiple algorithms for matrix multiplication
 * including sequential, multi-threaded, and parallel stream implementations.
 *
 * This class offers various optimization strategies suitable for different
 * matrix sizes and system configurations.
 *
 * @author İshak Duran
 * @version 1.0
 */
public class MatrixMultiplier {

  /**
   * Multiplies two matrices using sequential algorithm.
   *
   * Time Complexity: O(n³)
   * Space Complexity: O(1) additional space
   *
   * @param matrixA The first matrix
   * @param matrixB The second matrix
   * @return The result matrix
   * @throws IllegalArgumentException if matrices cannot be multiplied
   */
  public Matrix multiplySequential(Matrix matrixA, Matrix matrixB) {
    validateMultiplication(matrixA, matrixB);

    int rowsA = matrixA.getRows();
    int colsA = matrixA.getColumns();
    int colsB = matrixB.getColumns();

    Matrix result = new Matrix(rowsA, colsB);

    for (int i = 0; i < rowsA; i++) {
      for (int j = 0; j < colsB; j++) {
        int sum = 0;
        for (int k = 0; k < colsA; k++) {
          sum += matrixA.get(i, k) * matrixB.get(k, j);
        }
        result.set(i, j, sum);
      }
    }

    return result;
  }

  /**
   * Multiplies two matrices using multi-threading with row-based parallelization.
   *
   * Each thread processes complete rows of the result matrix.
   *
   * @param matrixA     The first matrix
   * @param matrixB     The second matrix
   * @param threadCount Number of threads to use
   * @return The result matrix
   * @throws IllegalArgumentException if matrices cannot be multiplied or
   *                                  threadCount is invalid
   */
  public Matrix multiplyThreaded(Matrix matrixA, Matrix matrixB, int threadCount) {
    validateMultiplication(matrixA, matrixB);
    validateThreadCount(threadCount);

    int rowsA = matrixA.getRows();
    int colsB = matrixB.getColumns();

    Matrix result = new Matrix(rowsA, colsB);

    // Adjust thread count if it's more than available rows
    threadCount = Math.min(threadCount, rowsA);

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    try {
      // Create tasks for each row
      @SuppressWarnings("unchecked")
      CompletableFuture<Void>[] futures = new CompletableFuture[rowsA];

      for (int i = 0; i < rowsA; i++) {
        final int row = i;
        futures[i] = CompletableFuture.runAsync(() -> {
          computeRow(matrixA, matrixB, result, row);
        }, executor);
      }

      // Wait for all tasks to complete
      CompletableFuture.allOf(futures).join();

    } finally {
      executor.shutdown();
      try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }

    return result;
  }

  /**
   * Multiplies two matrices using Java 8+ parallel streams.
   *
   * Automatically scales to available CPU cores.
   *
   * @param matrixA The first matrix
   * @param matrixB The second matrix
   * @return The result matrix
   * @throws IllegalArgumentException if matrices cannot be multiplied
   */
  public Matrix multiplyParallel(Matrix matrixA, Matrix matrixB) {
    validateMultiplication(matrixA, matrixB);

    int rowsA = matrixA.getRows();
    int colsB = matrixB.getColumns();

    Matrix result = new Matrix(rowsA, colsB);

    IntStream.range(0, rowsA).parallel().forEach(i -> {
      computeRow(matrixA, matrixB, result, i);
    });

    return result;
  }

  /**
   * Multiplies two matrices using block-based multi-threading for better cache
   * locality.
   *
   * Divides matrices into blocks and processes them in parallel.
   *
   * @param matrixA     The first matrix
   * @param matrixB     The second matrix
   * @param blockSize   Size of each block
   * @param threadCount Number of threads to use
   * @return The result matrix
   * @throws IllegalArgumentException if matrices cannot be multiplied or
   *                                  parameters are invalid
   */
  public Matrix multiplyBlockThreaded(Matrix matrixA, Matrix matrixB, int blockSize, int threadCount) {
    validateMultiplication(matrixA, matrixB);
    validateThreadCount(threadCount);

    if (blockSize <= 0) {
      throw new IllegalArgumentException("Block size must be positive");
    }

    int rowsA = matrixA.getRows();
    int colsA = matrixA.getColumns();
    int colsB = matrixB.getColumns();

    Matrix result = new Matrix(rowsA, colsB);
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    try {
      CompletableFuture<?>[] futures = new CompletableFuture[((rowsA + blockSize - 1) / blockSize)
          * ((colsB + blockSize - 1) / blockSize)];

      int futureIndex = 0;

      for (int i = 0; i < rowsA; i += blockSize) {
        for (int j = 0; j < colsB; j += blockSize) {
          final int startRow = i;
          final int startCol = j;
          final int endRow = Math.min(i + blockSize, rowsA);
          final int endCol = Math.min(j + blockSize, colsB);

          futures[futureIndex++] = CompletableFuture.runAsync(() -> {
            computeBlock(matrixA, matrixB, result, startRow, endRow, startCol, endCol, colsA);
          }, executor);
        }
      }

      CompletableFuture.allOf(futures).join();

    } finally {
      executor.shutdown();
      try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }

    return result;
  }

  /**
   * Computes a single row of the result matrix.
   *
   * @param matrixA The first matrix
   * @param matrixB The second matrix
   * @param result  The result matrix
   * @param row     The row to compute
   */
  private void computeRow(Matrix matrixA, Matrix matrixB, Matrix result, int row) {
    int colsA = matrixA.getColumns();
    int colsB = matrixB.getColumns();

    for (int j = 0; j < colsB; j++) {
      int sum = 0;
      for (int k = 0; k < colsA; k++) {
        sum += matrixA.get(row, k) * matrixB.get(k, j);
      }
      result.set(row, j, sum);
    }
  }

  /**
   * Computes a block of the result matrix for block-based multiplication.
   *
   * @param matrixA  The first matrix
   * @param matrixB  The second matrix
   * @param result   The result matrix
   * @param startRow Starting row of the block
   * @param endRow   Ending row of the block (exclusive)
   * @param startCol Starting column of the block
   * @param endCol   Ending column of the block (exclusive)
   * @param colsA    Number of columns in matrix A
   */
  private void computeBlock(Matrix matrixA, Matrix matrixB, Matrix result,
      int startRow, int endRow, int startCol, int endCol, int colsA) {
    for (int i = startRow; i < endRow; i++) {
      for (int j = startCol; j < endCol; j++) {
        int sum = 0;
        for (int k = 0; k < colsA; k++) {
          sum += matrixA.get(i, k) * matrixB.get(k, j);
        }
        result.set(i, j, sum);
      }
    }
  }

  /**
   * Validates that two matrices can be multiplied.
   *
   * @param matrixA The first matrix
   * @param matrixB The second matrix
   * @throws IllegalArgumentException if matrices cannot be multiplied
   */
  private void validateMultiplication(Matrix matrixA, Matrix matrixB) {
    if (matrixA == null) {
      throw new IllegalArgumentException("First matrix cannot be null");
    }
    if (matrixB == null) {
      throw new IllegalArgumentException("Second matrix cannot be null");
    }
    if (!matrixA.canMultiplyWith(matrixB)) {
      throw new IllegalArgumentException(
          String.format("Cannot multiply matrices: %dx%d × %dx%d. " +
              "Number of columns in first matrix (%d) must equal " +
              "number of rows in second matrix (%d)",
              matrixA.getRows(), matrixA.getColumns(),
              matrixB.getRows(), matrixB.getColumns(),
              matrixA.getColumns(), matrixB.getRows()));
    }
  }

  /**
   * Validates the thread count parameter.
   *
   * @param threadCount The number of threads
   * @throws IllegalArgumentException if thread count is invalid
   */
  private void validateThreadCount(int threadCount) {
    if (threadCount <= 0) {
      throw new IllegalArgumentException("Thread count must be positive");
    }
    if (threadCount > Runtime.getRuntime().availableProcessors() * 2) {
      System.out.println("Warning: Thread count (" + threadCount +
          ") is higher than recommended (" +
          Runtime.getRuntime().availableProcessors() * 2 + ")");
    }
  }

  /**
   * Gets the optimal thread count for the current system.
   *
   * @return Recommended thread count
   */
  public static int getOptimalThreadCount() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Gets the optimal block size for block-based multiplication.
   *
   * @param matrixSize The size of the matrices
   * @return Recommended block size
   */
  public static int getOptimalBlockSize(int matrixSize) {
    // Heuristic: block size should be around sqrt of matrix size
    // but adjusted for cache considerations
    if (matrixSize <= 100)
      return 16;
    if (matrixSize <= 500)
      return 32;
    if (matrixSize <= 1000)
      return 64;
    return 128;
  }
}
