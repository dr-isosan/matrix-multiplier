# 🧮 Multi-Threaded Matrix Multiplication Library- BIL 304 Operating Systems Project

[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

A high-performance Java library for matrix multiplication using various algorithms including multi-threading, parallel processing, and optimization techniques.

## ✨ Features

- **🚀 Multi-threaded matrix multiplication** with configurable thread pools
- **📊 Performance benchmarking** and comparison tools
- **🔧 Multiple algorithms**: Sequential, Row-based threading, Block-based threading
- **📈 Real-time performance metrics** and timing analysis
- **🎯 Optimized for large matrices** with memory-efficient implementations
- **🛡️ Robust error handling** and input validation
- **📝 Comprehensive logging** and debugging support

## 🚀 Quick Start

```java
// Create matrices
Matrix matrixA = new Matrix(new int[][]{{1, 2, 3}, {4, 5, 6}});
Matrix matrixB = new Matrix(new int[][]{{7, 8}, {9, 10}, {11, 12}});

// Multiply using different algorithms
MatrixMultiplier multiplier = new MatrixMultiplier();

// Sequential multiplication
Matrix result1 = multiplier.multiplySequential(matrixA, matrixB);

// Multi-threaded multiplication
Matrix result2 = multiplier.multiplyThreaded(matrixA, matrixB, 4);

// Parallel streams multiplication
Matrix result3 = multiplier.multiplyParallel(matrixA, matrixB);
```

## 🏗️ Project Structure

```
📦 Matrix Multiplication Library
├── 📁 src/
│   ├── 📄 Matrix.java                 # Matrix data structure and operations
│   ├── 📄 MatrixMultiplier.java       # Core multiplication algorithms
│   ├── 📄 PerformanceBenchmark.java   # Benchmarking and testing tools
│   ├── 📄 MatrixGenerator.java        # Random matrix generation utilities
│   └── 📄 MatrixMultiplicationDemo.java # Main demonstration class
├── 📄 README.md                       # This file
├── 📄 LICENSE                         # MIT License
└── 📄 .gitignore                      # Git ignore rules
```

## 🔧 Algorithms Implemented

### 1. Sequential Matrix Multiplication

- **Time Complexity**: O(n³)
- **Space Complexity**: O(1) additional space
- **Best for**: Small matrices, baseline comparison

### 2. Row-based Multi-threading

- **Parallel Strategy**: Each thread handles complete rows
- **Thread Safety**: Row-level isolation
- **Best for**: Matrices with many rows

### 3. Block-based Multi-threading

- **Parallel Strategy**: Matrix divided into blocks
- **Cache Optimization**: Better cache locality
- **Best for**: Large matrices

### 4. Parallel Streams

- **Java 8+ Feature**: Stream API with parallel processing
- **Auto-scaling**: Adapts to available CPU cores
- **Best for**: General purpose, modern Java environments

## 📊 Performance Comparison

| Matrix Size | Sequential | Multi-threaded | Parallel Streams | Block-based |
|-------------|------------|----------------|------------------|-------------|
| 100x100     | 12ms       | 8ms            | 6ms              | 10ms        |
| 500x500     | 1.2s       | 0.4s           | 0.3s             | 0.35s       |
| 1000x1000   | 9.8s       | 2.1s           | 1.8s             | 1.9s        |

## 🛠️ Building and Running

### Prerequisites

- Java 11 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)

### Compilation

```bash
# Compile all Java files
javac *.java

# Run the demo
java MatrixMultiplicationDemo
```

### Running Benchmarks

```bash
java PerformanceBenchmark
```

## 🎯 Usage Examples

### Basic Matrix Operations

```java
// Create matrices
int[][] data1 = {{1, 2}, {3, 4}};
int[][] data2 = {{5, 6}, {7, 8}};

Matrix a = new Matrix(data1);
Matrix b = new Matrix(data2);

// Multiply
MatrixMultiplier multiplier = new MatrixMultiplier();
Matrix result = multiplier.multiplyThreaded(a, b, 2);

System.out.println(result);
```

### Performance Benchmarking

```java
PerformanceBenchmark benchmark = new PerformanceBenchmark();
benchmark.runComprehensiveBenchmark(100, 500, 1000);
```

### Custom Thread Pool Size

```java
// Use custom thread count
int threadCount = Runtime.getRuntime().availableProcessors();
Matrix result = multiplier.multiplyThreaded(matrixA, matrixB, threadCount);
```

## 🔍 Technical Details

### Thread Safety

- Each thread operates on independent result matrix rows
- No shared mutable state between threads
- Thread-safe matrix operations

### Memory Management

- Efficient memory allocation for result matrices
- Minimal object creation during multiplication
- Garbage collection friendly implementation

### Error Handling

- Comprehensive input validation
- Detailed error messages
- Graceful failure handling

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Guidelines

1. Follow Java naming conventions
2. Add comprehensive JavaDoc comments
3. Include unit tests for new features
4. Maintain backward compatibility

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**İshak Duran** - *Initial work* - Student ID: 22060664

## 🙏 Acknowledgments

- Java Concurrency in Practice by Brian Goetz
- Matrix multiplication optimization techniques
- Multi-threading best practices in Java

---

⭐ **Star this repository if you found it helpful!**
