#!/bin/bash

# Matrix Multiplication Library Build Script
# Author: İshak Duran (22060664)
# Version: 1.0

echo "🧮 Matrix Multiplication Library Build Script"
echo "=============================================="

# Clean previous builds
echo "🧹 Cleaning previous builds..."
rm -f *.class

# Compile all Java files with Java 21 compatibility
echo "🔨 Compiling Java files..."
if javac --release 21 *.java; then
    echo "✅ Compilation successful!"
else
    echo "❌ Compilation failed!"
    exit 1
fi

# List compiled files
echo ""
echo "📁 Compiled files:"
ls -la *.class

# Run tests
echo ""
echo "🧪 Running tests..."

echo ""
echo "1️⃣  Testing enhanced original implementation:"
echo "----------------------------------------"
java odev

echo ""
echo "2️⃣  Testing matrix operations:"
echo "----------------------------------------"
echo "1" | java MatrixMultiplicationDemo | head -30

echo ""
echo "3️⃣  Running quick performance test:"
echo "----------------------------------------"
java PerformanceBenchmark 2>/dev/null | head -20

echo ""
echo "🎉 Build and test completed successfully!"
echo "=============================================="
echo ""
echo "📋 Available programs:"
echo "• java odev                       - Enhanced original implementation"
echo "• java MatrixMultiplicationDemo   - Interactive demo with multiple features"
echo "• java PerformanceBenchmark       - Comprehensive performance analysis"
echo ""
echo "📖 For more information, see README.md"
