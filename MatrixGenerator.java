import java.util.Random;

/**
 * MatrixGenerator provides utilities for generating matrices with various
 * patterns
 * and random data for testing and benchmarking purposes.
 *
 * @author İshak Duran
 * @version 1.0
 */
public class MatrixGenerator {
  private static final Random random = new Random();

  /**
   * Generates a matrix with random integers in the specified range.
   *
   * @param rows     Number of rows
   * @param columns  Number of columns
   * @param minValue Minimum value (inclusive)
   * @param maxValue Maximum value (exclusive)
   * @return A new Matrix with random values
   */
  public static Matrix random(int rows, int columns, int minValue, int maxValue) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Matrix dimensions must be positive");
    }
    if (minValue >= maxValue) {
      throw new IllegalArgumentException("Min value must be less than max value");
    }

    int[][] data = new int[rows][columns];
    int range = maxValue - minValue;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        data[i][j] = random.nextInt(range) + minValue;
      }
    }

    return new Matrix(data);
  }

  /**
   * Generates a matrix with random integers between 0 and 99.
   *
   * @param rows    Number of rows
   * @param columns Number of columns
   * @return A new Matrix with random values
   */
  public static Matrix random(int rows, int columns) {
    return random(rows, columns, 0, 100);
  }

  /**
   * Generates a square matrix with random values.
   *
   * @param size The size of the square matrix
   * @return A new square Matrix with random values
   */
  public static Matrix randomSquare(int size) {
    return random(size, size);
  }

  /**
   * Generates an identity matrix of the specified size.
   *
   * @param size The size of the identity matrix
   * @return A new identity Matrix
   */
  public static Matrix identity(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("Matrix size must be positive");
    }

    Matrix matrix = new Matrix(size, size);
    for (int i = 0; i < size; i++) {
      matrix.set(i, i, 1);
    }
    return matrix;
  }

  /**
   * Generates a matrix filled with zeros.
   *
   * @param rows    Number of rows
   * @param columns Number of columns
   * @return A new Matrix filled with zeros
   */
  public static Matrix zeros(int rows, int columns) {
    return new Matrix(rows, columns);
  }

  /**
   * Generates a matrix filled with ones.
   *
   * @param rows    Number of rows
   * @param columns Number of columns
   * @return A new Matrix filled with ones
   */
  public static Matrix ones(int rows, int columns) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Matrix dimensions must be positive");
    }

    Matrix matrix = new Matrix(rows, columns);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        matrix.set(i, j, 1);
      }
    }
    return matrix;
  }

  /**
   * Generates a matrix with a specific value.
   *
   * @param rows    Number of rows
   * @param columns Number of columns
   * @param value   The value to fill the matrix with
   * @return A new Matrix filled with the specified value
   */
  public static Matrix filled(int rows, int columns, int value) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Matrix dimensions must be positive");
    }

    Matrix matrix = new Matrix(rows, columns);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        matrix.set(i, j, value);
      }
    }
    return matrix;
  }

  /**
   * Generates a diagonal matrix with the specified values on the diagonal.
   *
   * @param values The values for the diagonal
   * @return A new diagonal Matrix
   */
  public static Matrix diagonal(int... values) {
    if (values == null || values.length == 0) {
      throw new IllegalArgumentException("Diagonal values cannot be null or empty");
    }

    int size = values.length;
    Matrix matrix = new Matrix(size, size);

    for (int i = 0; i < size; i++) {
      matrix.set(i, i, values[i]);
    }

    return matrix;
  }

  /**
   * Generates a sparse matrix with a specified density of non-zero elements.
   *
   * @param rows     Number of rows
   * @param columns  Number of columns
   * @param density  Density of non-zero elements (0.0 to 1.0)
   * @param minValue Minimum value for non-zero elements
   * @param maxValue Maximum value for non-zero elements
   * @return A new sparse Matrix
   */
  public static Matrix sparse(int rows, int columns, double density, int minValue, int maxValue) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Matrix dimensions must be positive");
    }
    if (density < 0.0 || density > 1.0) {
      throw new IllegalArgumentException("Density must be between 0.0 and 1.0");
    }
    if (minValue >= maxValue) {
      throw new IllegalArgumentException("Min value must be less than max value");
    }

    Matrix matrix = new Matrix(rows, columns);
    int range = maxValue - minValue;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (random.nextDouble() < density) {
          int value;
          do {
            value = random.nextInt(range) + minValue;
          } while (value == 0); // Ensure non-zero for sparse matrix

          matrix.set(i, j, value);
        }
      }
    }

    return matrix;
  }

  /**
   * Generates a matrix with a specific pattern for testing purposes.
   *
   * @param rows    Number of rows
   * @param columns Number of columns
   * @param pattern The pattern type
   * @return A new Matrix with the specified pattern
   */
  public static Matrix pattern(int rows, int columns, Pattern pattern) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Matrix dimensions must be positive");
    }

    Matrix matrix = new Matrix(rows, columns);

    switch (pattern) {
      case ASCENDING:
        int value = 1;
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < columns; j++) {
            matrix.set(i, j, value++);
          }
        }
        break;

      case DESCENDING:
        value = rows * columns;
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < columns; j++) {
            matrix.set(i, j, value--);
          }
        }
        break;

      case CHECKERBOARD:
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < columns; j++) {
            matrix.set(i, j, (i + j) % 2);
          }
        }
        break;

      case ROW_INDEX:
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < columns; j++) {
            matrix.set(i, j, i);
          }
        }
        break;

      case COLUMN_INDEX:
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < columns; j++) {
            matrix.set(i, j, j);
          }
        }
        break;
    }

    return matrix;
  }

  /**
   * Sets the seed for the random number generator for reproducible results.
   *
   * @param seed The seed value
   */
  public static void setSeed(long seed) {
    random.setSeed(seed);
  }

  /**
   * Enumeration of available matrix patterns.
   */
  public enum Pattern {
    ASCENDING, // 1, 2, 3, 4, ...
    DESCENDING, // n*m, n*m-1, n*m-2, ...
    CHECKERBOARD, // 0, 1, 0, 1, ...
    ROW_INDEX, // Row index at each position
    COLUMN_INDEX // Column index at each position
  }
}
