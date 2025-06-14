import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * PerformanceBenchmark provides comprehensive benchmarking tools for matrix
 * multiplication algorithms, including timing analysis, memory usage tracking,
 * and performance comparison utilities.
 *
 * @author İshak Duran
 * @version 1.0
 */
public class PerformanceBenchmark {
  private final MatrixMultiplier multiplier;

  public PerformanceBenchmark() {
    this.multiplier = new MatrixMultiplier();
  }

  /**
   * Represents the result of a benchmark test.
   */
  public static class BenchmarkResult {
    private final String algorithmName;
    private final long executionTimeNanos;
    private final int matrixSize;
    private final long memoryUsed;
    private final boolean successful;
    private final String errorMessage;

    public BenchmarkResult(String algorithmName, long executionTimeNanos,
        int matrixSize, long memoryUsed, boolean successful, String errorMessage) {
      this.algorithmName = algorithmName;
      this.executionTimeNanos = executionTimeNanos;
      this.matrixSize = matrixSize;
      this.memoryUsed = memoryUsed;
      this.successful = successful;
      this.errorMessage = errorMessage;
    }

    public String getAlgorithmName() {
      return algorithmName;
    }

    public long getExecutionTimeNanos() {
      return executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
      return executionTimeNanos / 1_000_000.0;
    }

    public double getExecutionTimeSeconds() {
      return executionTimeNanos / 1_000_000_000.0;
    }

    public int getMatrixSize() {
      return matrixSize;
    }

    public long getMemoryUsed() {
      return memoryUsed;
    }

    public boolean isSuccessful() {
      return successful;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    @Override
    public String toString() {
      if (!successful) {
        return String.format("%-20s: FAILED - %s", algorithmName, errorMessage);
      }
      return String.format("%-20s: %8.2f ms (%8.3f s) | Memory: %6.2f MB",
          algorithmName, getExecutionTimeMillis(), getExecutionTimeSeconds(),
          memoryUsed / (1024.0 * 1024.0));
    }
  }

  /**
   * Benchmarks a single algorithm with the given matrices.
   *
   * @param algorithmName Name of the algorithm
   * @param matrixA       First matrix
   * @param matrixB       Second matrix
   * @param algorithm     The algorithm to benchmark
   * @return BenchmarkResult containing timing and memory information
   */
  public BenchmarkResult benchmarkAlgorithm(String algorithmName, Matrix matrixA, Matrix matrixB,
      MatrixOperation algorithm) {
    // Force garbage collection before measurement
    System.gc();

    long memoryBefore = getUsedMemory();
    long startTime = System.nanoTime();

    boolean successful = true;
    String errorMessage = null;

    try {
      algorithm.multiply(matrixA, matrixB);
    } catch (Exception e) {
      successful = false;
      errorMessage = e.getMessage();
    }

    long endTime = System.nanoTime();
    long memoryAfter = getUsedMemory();

    long executionTime = endTime - startTime;
    long memoryUsed = Math.max(0, memoryAfter - memoryBefore);

    return new BenchmarkResult(algorithmName, executionTime, matrixA.getRows(),
        memoryUsed, successful, errorMessage);
  }

  /**
   * Runs a comprehensive benchmark comparing all available algorithms.
   *
   * @param sizes Array of matrix sizes to test
   */
  public void runComprehensiveBenchmark(int... sizes) {
    System.out.println("🧮 Matrix Multiplication Performance Benchmark");
    System.out.println(StringUtils.equals(70));

    for (int size : sizes) {
      System.out.printf("\n📊 Testing %dx%d matrices:\n", size, size);
      System.out.println(StringUtils.dashes(70));

      // Generate test matrices
      Matrix matrixA = MatrixGenerator.random(size, size, 1, 100);
      Matrix matrixB = MatrixGenerator.random(size, size, 1, 100);

      List<BenchmarkResult> results = new ArrayList<>();

      // Sequential algorithm
      results.add(benchmarkAlgorithm("Sequential", matrixA, matrixB,
          (a, b) -> multiplier.multiplySequential(a, b)));

      // Multi-threaded with different thread counts
      int[] threadCounts = { 2, 4, MatrixMultiplier.getOptimalThreadCount() };
      for (int threads : threadCounts) {
        results.add(benchmarkAlgorithm("Threaded (" + threads + ")", matrixA, matrixB,
            (a, b) -> multiplier.multiplyThreaded(a, b, threads)));
      }

      // Parallel streams
      results.add(benchmarkAlgorithm("Parallel Streams", matrixA, matrixB,
          (a, b) -> multiplier.multiplyParallel(a, b)));

      // Block-based threading
      int blockSize = MatrixMultiplier.getOptimalBlockSize(size);
      results.add(benchmarkAlgorithm("Block Threaded", matrixA, matrixB,
          (a, b) -> multiplier.multiplyBlockThreaded(a, b, blockSize,
              MatrixMultiplier.getOptimalThreadCount())));

      // Print results
      for (BenchmarkResult result : results) {
        System.out.println(result);
      }

      // Find and highlight the fastest algorithm
      BenchmarkResult fastest = results.stream()
          .filter(BenchmarkResult::isSuccessful)
          .min(Comparator.comparingLong(BenchmarkResult::getExecutionTimeNanos))
          .orElse(null);

      if (fastest != null) {
        System.out.printf("\n🏆 Fastest: %s (%.2f ms)\n",
            fastest.getAlgorithmName(), fastest.getExecutionTimeMillis());
      }
    }

    System.out.println("\n" + StringUtils.equals(70));
    System.out.println("✅ Benchmark completed successfully!");
  }

  /**
   * Benchmarks scalability by testing different matrix sizes.
   *
   * @param algorithm     The algorithm to test
   * @param algorithmName Name of the algorithm
   * @param startSize     Starting matrix size
   * @param endSize       Ending matrix size
   * @param step          Step size
   */
  public void benchmarkScalability(MatrixOperation algorithm, String algorithmName,
      int startSize, int endSize, int step) {
    System.out.printf("\n📈 Scalability Benchmark: %s\n", algorithmName);
    System.out.println(StringUtils.dashes(50));
    System.out.printf("%-10s | %-15s | %-10s\n", "Size", "Time (ms)", "Memory (MB)");
    System.out.println(StringUtils.dashes(50));

    for (int size = startSize; size <= endSize; size += step) {
      Matrix matrixA = MatrixGenerator.random(size, size, 1, 100);
      Matrix matrixB = MatrixGenerator.random(size, size, 1, 100);

      BenchmarkResult result = benchmarkAlgorithm(algorithmName, matrixA, matrixB, algorithm);

      if (result.isSuccessful()) {
        System.out.printf("%-10d | %-15.2f | %-10.2f\n",
            size, result.getExecutionTimeMillis(),
            result.getMemoryUsed() / (1024.0 * 1024.0));
      } else {
        System.out.printf("%-10d | FAILED: %s\n", size, result.getErrorMessage());
      }
    }
  }

  /**
   * Runs a warm-up phase to ensure JIT compilation and JVM optimizations.
   *
   * @param warmupRounds Number of warm-up rounds
   */
  public void warmUp(int warmupRounds) {
    System.out.println("🔥 Warming up JVM...");

    for (int i = 0; i < warmupRounds; i++) {
      Matrix a = MatrixGenerator.random(50, 50);
      Matrix b = MatrixGenerator.random(50, 50);

      multiplier.multiplySequential(a, b);
      multiplier.multiplyThreaded(a, b, 2);
      multiplier.multiplyParallel(a, b);
    }

    System.out.println("✅ Warm-up completed\n");
  }

  /**
   * Tests correctness of all algorithms by comparing results.
   *
   * @param testSize Size of matrices to use for testing
   * @return true if all algorithms produce the same result
   */
  public boolean testCorrectness(int testSize) {
    System.out.println("🔍 Testing algorithm correctness...");

    Matrix matrixA = MatrixGenerator.random(testSize, testSize, 1, 10);
    Matrix matrixB = MatrixGenerator.random(testSize, testSize, 1, 10);

    Matrix sequential = multiplier.multiplySequential(matrixA, matrixB);
    Matrix threaded = multiplier.multiplyThreaded(matrixA, matrixB, 2);
    Matrix parallel = multiplier.multiplyParallel(matrixA, matrixB);
    Matrix blockThreaded = multiplier.multiplyBlockThreaded(matrixA, matrixB, 16, 2);

    boolean allEqual = sequential.equals(threaded) &&
        sequential.equals(parallel) &&
        sequential.equals(blockThreaded);

    if (allEqual) {
      System.out.println("✅ All algorithms produce identical results");
    } else {
      System.out.println("❌ Algorithm results differ!");
    }

    return allEqual;
  }

  /**
   * Gets the current memory usage in bytes.
   *
   * @return Current memory usage
   */
  private long getUsedMemory() {
    Runtime runtime = Runtime.getRuntime();
    return runtime.totalMemory() - runtime.freeMemory();
  }

  /**
   * Functional interface for matrix multiplication algorithms.
   */
  @FunctionalInterface
  public interface MatrixOperation {
    Matrix multiply(Matrix a, Matrix b);
  }

  /**
   * Main method for running benchmarks.
   */
  public static void main(String[] args) {
    PerformanceBenchmark benchmark = new PerformanceBenchmark();

    // Warm up the JVM
    benchmark.warmUp(5);

    // Test correctness
    if (!benchmark.testCorrectness(10)) {
      System.err.println("Correctness test failed! Exiting...");
      return;
    }

    // Run comprehensive benchmark
    benchmark.runComprehensiveBenchmark(50, 100, 200, 300);

    // Run scalability test for the best performing algorithm
    System.out.println("\n" + StringUtils.equals(70));
    benchmark.benchmarkScalability(
        (a, b) -> benchmark.multiplier.multiplyParallel(a, b),
        "Parallel Streams",
        50, 300, 50);
  }
}
