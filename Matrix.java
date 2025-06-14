/**
 * Matrix class representing a 2D matrix with comprehensive operations and
 * validation.
 *
 * This class provides a robust implementation of matrix operations with proper
 * error handling, validation, and performance optimizations.
 *
 * @author İshak Duran
 * @version 1.0
 */
public class Matrix {
  private final int[][] data;
  private final int rows;
  private final int columns;

  /**
   * Creates a new Matrix from a 2D array.
   *
   * @param data The 2D array representing the matrix
   * @throws IllegalArgumentException if data is null, empty, or jagged
   */
  public Matrix(int[][] data) {
    validateInput(data);
    this.rows = data.length;
    this.columns = data[0].length;

    // Deep copy to ensure immutability
    this.data = new int[rows][columns];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(data[i], 0, this.data[i], 0, columns);
    }
  }

  /**
   * Creates a new Matrix with specified dimensions, initialized to zero.
   *
   * @param rows    Number of rows
   * @param columns Number of columns
   * @throws IllegalArgumentException if dimensions are not positive
   */
  public Matrix(int rows, int columns) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException(
          "Matrix dimensions must be positive. Got: " + rows + "x" + columns);
    }

    this.rows = rows;
    this.columns = columns;
    this.data = new int[rows][columns];
  }

  /**
   * Validates the input matrix data for consistency and correctness.
   *
   * @param data The matrix data to validate
   * @throws IllegalArgumentException if data is invalid
   */
  private void validateInput(int[][] data) {
    if (data == null) {
      throw new IllegalArgumentException("Matrix data cannot be null");
    }

    if (data.length == 0) {
      throw new IllegalArgumentException("Matrix cannot be empty");
    }

    int expectedColumns = data[0].length;
    if (expectedColumns == 0) {
      throw new IllegalArgumentException("Matrix rows cannot be empty");
    }

    // Check for jagged arrays
    for (int i = 0; i < data.length; i++) {
      if (data[i] == null) {
        throw new IllegalArgumentException("Matrix row " + i + " cannot be null");
      }
      if (data[i].length != expectedColumns) {
        throw new IllegalArgumentException(
            "Matrix must be rectangular. Row " + i + " has " +
                data[i].length + " columns, expected " + expectedColumns);
      }
    }
  }

  /**
   * Gets the number of rows in the matrix.
   *
   * @return The number of rows
   */
  public int getRows() {
    return rows;
  }

  /**
   * Gets the number of columns in the matrix.
   *
   * @return The number of columns
   */
  public int getColumns() {
    return columns;
  }

  /**
   * Gets the element at the specified position.
   *
   * @param row    The row index (0-based)
   * @param column The column index (0-based)
   * @return The element at the specified position
   * @throws IndexOutOfBoundsException if indices are out of bounds
   */
  public int get(int row, int column) {
    validateIndices(row, column);
    return data[row][column];
  }

  /**
   * Sets the element at the specified position.
   *
   * @param row    The row index (0-based)
   * @param column The column index (0-based)
   * @param value  The value to set
   * @throws IndexOutOfBoundsException if indices are out of bounds
   */
  public void set(int row, int column, int value) {
    validateIndices(row, column);
    data[row][column] = value;
  }

  /**
   * Validates that the given indices are within bounds.
   *
   * @param row    The row index
   * @param column The column index
   * @throws IndexOutOfBoundsException if indices are out of bounds
   */
  private void validateIndices(int row, int column) {
    if (row < 0 || row >= rows) {
      throw new IndexOutOfBoundsException(
          "Row index " + row + " is out of bounds [0, " + (rows - 1) + "]");
    }
    if (column < 0 || column >= columns) {
      throw new IndexOutOfBoundsException(
          "Column index " + column + " is out of bounds [0, " + (columns - 1) + "]");
    }
  }

  /**
   * Gets a copy of the entire row.
   *
   * @param row The row index
   * @return A copy of the row data
   * @throws IndexOutOfBoundsException if row index is out of bounds
   */
  public int[] getRow(int row) {
    if (row < 0 || row >= rows) {
      throw new IndexOutOfBoundsException(
          "Row index " + row + " is out of bounds [0, " + (rows - 1) + "]");
    }

    int[] rowCopy = new int[columns];
    System.arraycopy(data[row], 0, rowCopy, 0, columns);
    return rowCopy;
  }

  /**
   * Gets a copy of the entire column.
   *
   * @param column The column index
   * @return A copy of the column data
   * @throws IndexOutOfBoundsException if column index is out of bounds
   */
  public int[] getColumn(int column) {
    if (column < 0 || column >= columns) {
      throw new IndexOutOfBoundsException(
          "Column index " + column + " is out of bounds [0, " + (columns - 1) + "]");
    }

    int[] columnData = new int[rows];
    for (int i = 0; i < rows; i++) {
      columnData[i] = data[i][column];
    }
    return columnData;
  }

  /**
   * Creates a deep copy of this matrix.
   *
   * @return A new Matrix instance with the same data
   */
  public Matrix copy() {
    return new Matrix(data);
  }

  /**
   * Checks if this matrix can be multiplied with another matrix.
   *
   * @param other The matrix to multiply with
   * @return true if multiplication is possible, false otherwise
   */
  public boolean canMultiplyWith(Matrix other) {
    return other != null && this.columns == other.rows;
  }

  /**
   * Transposes the matrix.
   *
   * @return A new Matrix that is the transpose of this matrix
   */
  public Matrix transpose() {
    Matrix transposed = new Matrix(columns, rows);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        transposed.set(j, i, data[i][j]);
      }
    }
    return transposed;
  }

  /**
   * Checks if this matrix is square.
   *
   * @return true if the matrix is square, false otherwise
   */
  public boolean isSquare() {
    return rows == columns;
  }

  /**
   * Returns a formatted string representation of the matrix.
   *
   * @return A string representation of the matrix
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Matrix ").append(rows).append("x").append(columns).append(":\n");

    // Calculate the maximum width needed for formatting
    int maxWidth = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        maxWidth = Math.max(maxWidth, String.valueOf(data[i][j]).length());
      }
    }

    // Build the formatted string
    for (int i = 0; i < rows; i++) {
      sb.append("│ ");
      for (int j = 0; j < columns; j++) {
        sb.append(String.format("%" + maxWidth + "d", data[i][j]));
        if (j < columns - 1) {
          sb.append("  ");
        }
      }
      sb.append(" │\n");
    }

    return sb.toString();
  }

  /**
   * Checks if this matrix is equal to another object.
   *
   * @param obj The object to compare with
   * @return true if the matrices are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Matrix matrix = (Matrix) obj;

    if (rows != matrix.rows || columns != matrix.columns) {
      return false;
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (data[i][j] != matrix.data[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Returns the hash code for this matrix.
   *
   * @return The hash code
   */
  @Override
  public int hashCode() {
    int result = Integer.hashCode(rows);
    result = 31 * result + Integer.hashCode(columns);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        result = 31 * result + Integer.hashCode(data[i][j]);
      }
    }

    return result;
  }
}
