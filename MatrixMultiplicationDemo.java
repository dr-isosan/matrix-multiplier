import java.util.Scanner;

/**
 * MatrixMultiplicationDemo is the main class that demonstrates the capabilities
 * of the Multi-Threaded Matrix Multiplication Library.
 *
 * This interactive demo allows users to explore different algorithms,
 * run benchmarks, and see performance comparisons.
 *
 * @author İshak Duran
 * @version 1.0
 */
public class MatrixMultiplicationDemo {
  private static final MatrixMultiplier multiplier = new MatrixMultiplier();
  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    printWelcomeMessage();

    boolean running = true;
    while (running) {
      printMainMenu();
      int choice = getIntInput("Enter your choice: ");

      switch (choice) {
        case 1:
          demonstrateBasicMultiplication();
          break;
        case 2:
          compareAlgorithms();
          break;
        case 3:
          runPerformanceBenchmark();
          break;
        case 4:
          demonstrateMatrixOperations();
          break;
        case 5:
          interactiveMatrixCreation();
          break;
        case 6:
          demonstrateErrorHandling();
          break;
        case 7:
          showSystemInfo();
          break;
        case 0:
          running = false;
          System.out.println("👋 Thank you for using the Matrix Multiplication Library!");
          break;
        default:
          System.out.println("❌ Invalid choice. Please try again.");
      }

      if (running) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
      }
    }

    scanner.close();
  }

  /**
   * Prints a beautiful welcome message with ASCII art.
   */
  private static void printWelcomeMessage() {
    System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                                                                      ║");
    System.out.println("║    🧮  MULTI-THREADED MATRIX MULTIPLICATION LIBRARY  🧮             ║");
    System.out.println("║                                                                      ║");
    System.out.println("║    Author: İshak Duran (22060664)                                   ║");
    System.out.println("║    A high-performance Java library for matrix multiplication        ║");
    System.out.println("║                                                                      ║");
    System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    System.out.println();
  }

  /**
   * Prints the main menu options.
   */
  private static void printMainMenu() {
    System.out.println("\n" + StringUtils.equals(50));
    System.out.println("📋 MAIN MENU");
    System.out.println(StringUtils.equals(50));
    System.out.println("1. 🚀 Basic Matrix Multiplication Demo");
    System.out.println("2. ⚡ Algorithm Comparison");
    System.out.println("3. 📊 Performance Benchmark");
    System.out.println("4. 🔧 Matrix Operations Demo");
    System.out.println("5. ✏️  Interactive Matrix Creation");
    System.out.println("6. 🛡️  Error Handling Demo");
    System.out.println("7. 💻 System Information");
    System.out.println("0. 🚪 Exit");
    System.out.println(StringUtils.equals(50));
  }

  /**
   * Demonstrates basic matrix multiplication with predefined matrices.
   */
  private static void demonstrateBasicMultiplication() {
    System.out.println("\n🚀 BASIC MATRIX MULTIPLICATION DEMO");
    System.out.println(StringUtils.dashes(40));

    // Create sample matrices
    Matrix matrixA = new Matrix(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
    Matrix matrixB = new Matrix(new int[][] { { 7, 8 }, { 9, 10 }, { 11, 12 } });

    System.out.println("Matrix A:");
    System.out.println(matrixA);

    System.out.println("Matrix B:");
    System.out.println(matrixB);

    // Multiply using different algorithms
    long startTime = System.nanoTime();
    Matrix result = multiplier.multiplySequential(matrixA, matrixB);
    long endTime = System.nanoTime();

    System.out.println("Result (A × B):");
    System.out.println(result);

    System.out.printf("⏱️  Execution time: %.3f ms\n", (endTime - startTime) / 1_000_000.0);
    System.out.println("✅ Multiplication completed successfully!");
  }

  /**
   * Compares different multiplication algorithms.
   */
  private static void compareAlgorithms() {
    System.out.println("\n⚡ ALGORITHM COMPARISON");
    System.out.println(StringUtils.dashes(40));

    int size = getIntInput("Enter matrix size (e.g., 100): ");
    if (size <= 0 || size > 1000) {
      System.out.println("❌ Invalid size. Using default size of 100.");
      size = 100;
    }

    System.out.println("🔄 Generating random " + size + "x" + size + " matrices...");
    Matrix matrixA = MatrixGenerator.random(size, size, 1, 100);
    Matrix matrixB = MatrixGenerator.random(size, size, 1, 100);

    System.out.println("\n📊 Comparing algorithms:");
    System.out.println(StringUtils.dashes(60));

    // Sequential
    long startTime = System.nanoTime();
    Matrix result1 = multiplier.multiplySequential(matrixA, matrixB);
    long endTime = System.nanoTime();
    System.out.printf("Sequential:        %8.2f ms\n", (endTime - startTime) / 1_000_000.0);

    // Multi-threaded
    startTime = System.nanoTime();
    Matrix result2 = multiplier.multiplyThreaded(matrixA, matrixB, 4);
    endTime = System.nanoTime();
    System.out.printf("Multi-threaded:    %8.2f ms\n", (endTime - startTime) / 1_000_000.0);

    // Parallel streams
    startTime = System.nanoTime();
    Matrix result3 = multiplier.multiplyParallel(matrixA, matrixB);
    endTime = System.nanoTime();
    System.out.printf("Parallel Streams:  %8.2f ms\n", (endTime - startTime) / 1_000_000.0);

    // Verify results are identical
    boolean identical = result1.equals(result2) && result1.equals(result3);
    System.out.println("\n🔍 Result verification: " + (identical ? "✅ All results identical" : "❌ Results differ"));
  }

  /**
   * Runs a comprehensive performance benchmark.
   */
  private static void runPerformanceBenchmark() {
    System.out.println("\n📊 PERFORMANCE BENCHMARK");
    System.out.println(StringUtils.dashes(40));

    System.out.println("This will run a comprehensive benchmark comparing all algorithms.");
    System.out.print("Continue? (y/n): ");

    if (!scanner.nextLine().toLowerCase().startsWith("y")) {
      return;
    }

    PerformanceBenchmark benchmark = new PerformanceBenchmark();

    // Warm up
    System.out.println("\n🔥 Warming up JVM...");
    benchmark.warmUp(3);

    // Test correctness
    if (!benchmark.testCorrectness(10)) {
      System.out.println("❌ Correctness test failed!");
      return;
    }

    // Run benchmark
    benchmark.runComprehensiveBenchmark(50, 100, 200);
  }

  /**
   * Demonstrates various matrix operations.
   */
  private static void demonstrateMatrixOperations() {
    System.out.println("\n🔧 MATRIX OPERATIONS DEMO");
    System.out.println(StringUtils.dashes(40));

    // Create a sample matrix
    Matrix matrix = MatrixGenerator.pattern(4, 4, MatrixGenerator.Pattern.ASCENDING);

    System.out.println("Original Matrix:");
    System.out.println(matrix);

    System.out.println("Transposed Matrix:");
    System.out.println(matrix.transpose());

    System.out.println("Matrix Properties:");
    System.out.println("- Rows: " + matrix.getRows());
    System.out.println("- Columns: " + matrix.getColumns());
    System.out.println("- Is Square: " + matrix.isSquare());

    // Generate different matrix types
    System.out.println("\nDifferent Matrix Types:");

    System.out.println("Identity Matrix (3x3):");
    System.out.println(MatrixGenerator.identity(3));

    System.out.println("Random Matrix (3x3):");
    System.out.println(MatrixGenerator.random(3, 3, 1, 10));

    System.out.println("Checkerboard Pattern (4x4):");
    System.out.println(MatrixGenerator.pattern(4, 4, MatrixGenerator.Pattern.CHECKERBOARD));
  }

  /**
   * Allows interactive matrix creation and multiplication.
   */
  private static void interactiveMatrixCreation() {
    System.out.println("\n✏️  INTERACTIVE MATRIX CREATION");
    System.out.println(StringUtils.dashes(40));

    try {
      System.out.println("Create Matrix A:");
      Matrix matrixA = createMatrixInteractively();

      System.out.println("\nCreate Matrix B:");
      Matrix matrixB = createMatrixInteractively();

      if (!matrixA.canMultiplyWith(matrixB)) {
        System.out.println("❌ These matrices cannot be multiplied!");
        System.out.printf("Matrix A: %dx%d, Matrix B: %dx%d\n",
            matrixA.getRows(), matrixA.getColumns(),
            matrixB.getRows(), matrixB.getColumns());
        return;
      }

      System.out.println("\nMatrix A:");
      System.out.println(matrixA);

      System.out.println("Matrix B:");
      System.out.println(matrixB);

      Matrix result = multiplier.multiplySequential(matrixA, matrixB);
      System.out.println("Result (A × B):");
      System.out.println(result);

    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
    }
  }

  /**
   * Creates a matrix interactively by asking user for input.
   */
  private static Matrix createMatrixInteractively() {
    int rows = getIntInput("Enter number of rows: ");
    int cols = getIntInput("Enter number of columns: ");

    if (rows <= 0 || cols <= 0) {
      throw new IllegalArgumentException("Dimensions must be positive");
    }

    Matrix matrix = new Matrix(rows, cols);

    System.out.println("Enter matrix elements:");
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        int value = getIntInput(String.format("Element [%d][%d]: ", i, j));
        matrix.set(i, j, value);
      }
    }

    return matrix;
  }

  /**
   * Demonstrates error handling capabilities.
   */
  private static void demonstrateErrorHandling() {
    System.out.println("\n🛡️  ERROR HANDLING DEMO");
    System.out.println(StringUtils.dashes(40));

    System.out.println("1. Attempting to multiply incompatible matrices:");
    try {
      Matrix a = new Matrix(new int[][] { { 1, 2 }, { 3, 4 } }); // 2x2
      Matrix b = new Matrix(new int[][] { { 1, 2, 3 } }); // 1x3
      multiplier.multiplySequential(a, b);
    } catch (IllegalArgumentException e) {
      System.out.println("✅ Caught expected error: " + e.getMessage());
    }

    System.out.println("\n2. Attempting to create invalid matrix:");
    try {
      new Matrix(new int[][] { { 1, 2 }, { 3 } }); // Jagged array
    } catch (IllegalArgumentException e) {
      System.out.println("✅ Caught expected error: " + e.getMessage());
    }

    System.out.println("\n3. Attempting invalid matrix access:");
    try {
      Matrix matrix = new Matrix(2, 2);
      matrix.get(5, 5); // Out of bounds
    } catch (IndexOutOfBoundsException e) {
      System.out.println("✅ Caught expected error: " + e.getMessage());
    }

    System.out.println("\n✅ All error handling tests passed!");
  }

  /**
   * Shows system information relevant to performance.
   */
  private static void showSystemInfo() {
    System.out.println("\n💻 SYSTEM INFORMATION");
    System.out.println(StringUtils.dashes(40));

    Runtime runtime = Runtime.getRuntime();

    System.out.println("Java Information:");
    System.out.println("- Version: " + System.getProperty("java.version"));
    System.out.println("- Vendor: " + System.getProperty("java.vendor"));
    System.out.println("- VM: " + System.getProperty("java.vm.name"));

    System.out.println("\nSystem Information:");
    System.out.println("- OS: " + System.getProperty("os.name"));
    System.out.println("- Architecture: " + System.getProperty("os.arch"));
    System.out.println("- Available Processors: " + runtime.availableProcessors());

    System.out.println("\nMemory Information:");
    long maxMemory = runtime.maxMemory();
    long totalMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();
    long usedMemory = totalMemory - freeMemory;

    System.out.printf("- Max Memory: %.2f MB\n", maxMemory / (1024.0 * 1024.0));
    System.out.printf("- Total Memory: %.2f MB\n", totalMemory / (1024.0 * 1024.0));
    System.out.printf("- Used Memory: %.2f MB\n", usedMemory / (1024.0 * 1024.0));
    System.out.printf("- Free Memory: %.2f MB\n", freeMemory / (1024.0 * 1024.0));

    System.out.println("\nRecommended Settings:");
    System.out.println("- Optimal Thread Count: " + MatrixMultiplier.getOptimalThreadCount());
    System.out.println("- Optimal Block Size (100x100): " + MatrixMultiplier.getOptimalBlockSize(100));
    System.out.println("- Optimal Block Size (500x500): " + MatrixMultiplier.getOptimalBlockSize(500));
  }

  /**
   * Gets integer input from user with error handling.
   */
  private static int getIntInput(String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
      } catch (NumberFormatException e) {
        System.out.println("❌ Please enter a valid integer.");
      }
    }
  }
}
