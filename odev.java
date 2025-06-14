
/**
 * Original Implementation - Enhanced Version
 *
 * This is the enhanced version of the original matrix multiplication assignment.
 * It maintains the original structure while adding better error handling,
 * documentation, and performance improvements.
 *
 * @author İshak Duran (22060664)
 * @version 2.0 - Enhanced for GitHub
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class odev {

    /**
     * Main method demonstrating enhanced matrix multiplication with threading.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Sample matrices for demonstration
        int[][] firstMatrix = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        };
        int[][] secondMatrix = {
                { 9, 8, 7 },
                { 6, 5, 4 },
                { 3, 2, 1 }
        };
        System.out.println("🧮 Enhanced Matrix Multiplication Demo");
        System.out.println("==================================================");

        displayMatrix("First Matrix", firstMatrix);
        displayMatrix("Second Matrix", secondMatrix);

        // Validate matrix multiplication compatibility
        if (isValidForMultiplication(firstMatrix, secondMatrix)) {
            // Record start time with higher precision
            long startTime = System.nanoTime();

            int[][] resultMatrix = multiplyMatrixWithThreads(firstMatrix, secondMatrix);

            // Calculate execution time
            long endTime = System.nanoTime();
            double processTimeMs = (endTime - startTime) / 1_000_000.0; // Display results
            System.out.println("\n📊 Results:");
            System.out.println("------------------------------");
            displayMatrix("Result Matrix", resultMatrix);
            System.out.printf("⏱️  Total execution time: %.3f ms\n", processTimeMs);
            System.out.println("✅ Matrix multiplication completed successfully!");

        } else {
            System.out.println("❌ Error: Matrices are not compatible for multiplication!");
            System.out.printf("First matrix: %dx%d, Second matrix: %dx%d\n",
                    firstMatrix.length, firstMatrix[0].length,
                    secondMatrix.length, secondMatrix[0].length);
            System.out.println("Requirement: Columns of first matrix must equal rows of second matrix.");
        }
    }

    /**
     * Validates if two matrices can be multiplied.
     * For matrices A(m×n) and B(p×q), multiplication is valid if n = p.
     *
     * @param firstMatrix  The first matrix (A)
     * @param secondMatrix The second matrix (B)
     * @return true if matrices can be multiplied, false otherwise
     */
    public static boolean isValidForMultiplication(int[][] firstMatrix, int[][] secondMatrix) {
        if (firstMatrix == null || secondMatrix == null) {
            return false;
        }
        if (firstMatrix.length == 0 || secondMatrix.length == 0) {
            return false;
        }
        if (firstMatrix[0] == null || secondMatrix[0] == null) {
            return false;
        }

        return firstMatrix[0].length == secondMatrix.length;
    }

    /**
     * Multiplies two matrices using multi-threading approach.
     * Each thread handles one row of the result matrix.
     *
     * @param firstMatrix  The first matrix (m×n)
     * @param secondMatrix The second matrix (n×p)
     * @return The result matrix (m×p)
     */
    public static int[][] multiplyMatrixWithThreads(int[][] firstMatrix, int[][] secondMatrix) {
        int rows = firstMatrix.length;
        int cols = secondMatrix[0].length;
        int[][] resultMatrix = new int[rows][cols];

        // Use thread pool for better resource management
        ExecutorService executor = Executors.newFixedThreadPool(
                Math.min(rows, Runtime.getRuntime().availableProcessors()));

        long[] threadTimes = new long[rows];

        System.out.println("\n🔄 Starting threaded multiplication...");
        System.out.println("Threads used: " + Math.min(rows, Runtime.getRuntime().availableProcessors()));

        try {
            for (int i = 0; i < rows; i++) {
                final int rowIndex = i;

                executor.submit(() -> {
                    long threadStartTime = System.nanoTime();

                    // Create and run the matrix row calculation
                    MatrixRowCalculator calculator = new MatrixRowCalculator(
                            rowIndex, firstMatrix, secondMatrix, resultMatrix);
                    calculator.run();

                    long threadEndTime = System.nanoTime();
                    threadTimes[rowIndex] = threadEndTime - threadStartTime;
                    System.out.printf("Thread %s completed row %d in %.3f ms\n",
                            Thread.currentThread().getName(), rowIndex + 1,
                            threadTimes[rowIndex] / 1_000_000.0);
                });
            }

            // Wait for all threads to complete
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                System.err.println("⚠️  Warning: Some threads did not complete within timeout");
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.err.println("❌ Error: Thread execution was interrupted");
        }

        // Display thread performance statistics
        displayThreadStatistics(threadTimes);

        return resultMatrix;
    }

    /**
     * Displays a matrix with proper formatting and labels.
     *
     * @param label  The label for the matrix
     * @param matrix The matrix to display
     */
    public static void displayMatrix(String label, int[][] matrix) {
        System.out.println("\n" + label + " (" + matrix.length + "×" + matrix[0].length + "):");

        // Calculate maximum width for formatting
        int maxWidth = 0;
        for (int[] row : matrix) {
            for (int value : row) {
                maxWidth = Math.max(maxWidth, String.valueOf(value).length());
            }
        }

        // Print matrix with proper alignment
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("│ ");
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%" + maxWidth + "d", matrix[i][j]);
                if (j < matrix[0].length - 1) {
                    System.out.print("  ");
                }
            }
            System.out.println(" │");
        }
    }

    /**
     * Displays thread performance statistics.
     *
     * @param threadTimes Array of thread execution times in nanoseconds
     */
    private static void displayThreadStatistics(long[] threadTimes) {
        System.out.println("\n📈 Thread Performance Statistics:");
        System.out.println("----------------------------------------");

        long totalTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;

        for (int i = 0; i < threadTimes.length; i++) {
            double timeMs = threadTimes[i] / 1_000_000.0;
            System.out.printf("Thread %2d: %8.3f ms\n", i + 1, timeMs);

            totalTime += threadTimes[i];
            minTime = Math.min(minTime, threadTimes[i]);
            maxTime = Math.max(maxTime, threadTimes[i]);
        }
        double avgTime = totalTime / (double) threadTimes.length / 1_000_000.0;
        System.out.println("----------------------------------------");
        System.out.printf("Average:   %8.3f ms\n", avgTime);
        System.out.printf("Minimum:   %8.3f ms\n", minTime / 1_000_000.0);
        System.out.printf("Maximum:   %8.3f ms\n", maxTime / 1_000_000.0);
    }
}

/**
 * Enhanced version of the thread logic for matrix row calculation.
 *
 * This class encapsulates the logic for calculating one row of the result
 * matrix
 * in a thread-safe manner with better error handling and performance tracking.
 */
class MatrixRowCalculator implements Runnable {
    private final int rowIndex;
    private final int[][] firstMatrix;
    private final int[][] secondMatrix;
    private final int[][] resultMatrix;

    /**
     * Constructor for matrix row calculator.
     *
     * @param rowIndex     The index of the row to calculate
     * @param firstMatrix  The first matrix
     * @param secondMatrix The second matrix
     * @param resultMatrix The result matrix to populate
     */
    public MatrixRowCalculator(int rowIndex, int[][] firstMatrix,
            int[][] secondMatrix, int[][] resultMatrix) {
        this.rowIndex = rowIndex;
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
    }

    /**
     * Calculates one row of the matrix multiplication result.
     * This method is thread-safe as each thread works on a different row.
     */
    @Override
    public void run() {
        try {
            int firstMatrixCols = firstMatrix[0].length;
            int secondMatrixCols = secondMatrix[0].length;

            // Calculate each element in the assigned row
            for (int j = 0; j < secondMatrixCols; j++) {
                int sum = 0;
                for (int k = 0; k < firstMatrixCols; k++) {
                    sum += firstMatrix[rowIndex][k] * secondMatrix[k][j];
                }
                resultMatrix[rowIndex][j] = sum;
            }

        } catch (Exception e) {
            System.err.printf("❌ Error in thread calculating row %d: %s\n",
                    rowIndex, e.getMessage());
        }
    }
}
