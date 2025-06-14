# 🚀 Getting Started Guide

Welcome to the Multi-Threaded Matrix Multiplication Library! This guide will help you quickly get up and running with the library.

## 📋 Prerequisites

- **Java 21 or higher** (OpenJDK recommended)
- **Terminal/Command Line** access
- **Basic understanding of Java** and matrix operations

## ⚡ Quick Start

### 1. Build the Project

```bash
# Make the build script executable (Linux/macOS)
chmod +x build.sh

# Run the build script
./build.sh
```

**Or compile manually:**

```bash
# Clean and compile
rm -f *.class
javac --release 21 *.java
```

### 2. Run the Applications

#### 🎯 Enhanced Original Implementation

```bash
java odev
```

This runs the enhanced version of the original assignment with beautiful output and performance metrics.

#### 🎮 Interactive Demo

```bash
java MatrixMultiplicationDemo
```

This launches an interactive menu where you can:

- Try different multiplication algorithms
- Run performance comparisons
- Create custom matrices
- Test error handling
- View system information

#### 📊 Performance Benchmark

```bash
java PerformanceBenchmark
```

This runs comprehensive benchmarks comparing all algorithms with detailed timing and memory usage.

## 🧮 Usage Examples

### Basic Matrix Multiplication

```java
// Create matrices
Matrix matrixA = new Matrix(new int[][]{{1, 2}, {3, 4}});
Matrix matrixB = new Matrix(new int[][]{{5, 6}, {7, 8}});

// Multiply using different algorithms
MatrixMultiplier multiplier = new MatrixMultiplier();

// Sequential (single-threaded)
Matrix result1 = multiplier.multiplySequential(matrixA, matrixB);

// Multi-threaded with 4 threads
Matrix result2 = multiplier.multiplyThreaded(matrixA, matrixB, 4);

// Parallel streams (automatic scaling)
Matrix result3 = multiplier.multiplyParallel(matrixA, matrixB);

// Block-based threading
Matrix result4 = multiplier.multiplyBlockThreaded(matrixA, matrixB, 16, 4);
```

## 🎯 Performance Tips

### Choosing the Right Algorithm

- **Small matrices (< 100x100)**: Use `multiplySequential()` - lowest overhead
- **Medium matrices (100x500)**: Use `multiplyParallel()` - automatic optimization
- **Large matrices (> 500x500)**: Use `multiplyThreaded()` with optimal thread count
- **Very large matrices**: Use `multiplyBlockThreaded()` for better cache locality

### Optimal Thread Count

```java
// Get system-optimal thread count
int optimalThreads = MatrixMultiplier.getOptimalThreadCount();

// Use it for multiplication
Matrix result = multiplier.multiplyThreaded(matrixA, matrixB, optimalThreads);
```

## 🔧 Troubleshooting

### Common Issues

1. **Java Version Error**

   ```
   UnsupportedClassVersionError: class file version X.0
   ```

   **Solution**: Recompile with correct Java version: `javac --release 21 *.java`

2. **Out of Memory Error**

   ```
   java.lang.OutOfMemoryError: Java heap space
   ```

   **Solution**: Increase heap size: `java -Xmx4g MyClass` or use smaller matrices

3. **Thread Pool Issues**
   - **Problem**: Too many threads causing slowdown
   - **Solution**: Use `MatrixMultiplier.getOptimalThreadCount()` or reduce thread count

---

🎉 **Happy Matrix Multiplying!** 🧮
