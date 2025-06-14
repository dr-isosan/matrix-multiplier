/**
 * Utility class for common string operations and formatting.
 *
 * @author İshak Duran
 * @version 1.0
 */
public class StringUtils {

  /**
   * Repeats a string a specified number of times.
   *
   * @param str   The string to repeat
   * @param count The number of times to repeat
   * @return The repeated string
   */
  public static String repeat(String str, int count) {
    if (count <= 0)
      return "";
    if (count == 1)
      return str;

    StringBuilder sb = new StringBuilder(str.length() * count);
    for (int i = 0; i < count; i++) {
      sb.append(str);
    }
    return sb.toString();
  }

  /**
   * Creates a separator line with the specified character and length.
   *
   * @param character The character to use
   * @param length    The length of the line
   * @return The separator line
   */
  public static String separator(char character, int length) {
    return repeat(String.valueOf(character), length);
  }

  /**
   * Creates a line of equals signs.
   *
   * @param length The length of the line
   * @return The equals line
   */
  public static String equals(int length) {
    return separator('=', length);
  }

  /**
   * Creates a line of dashes.
   *
   * @param length The length of the line
   * @return The dash line
   */
  public static String dashes(int length) {
    return separator('-', length);
  }
}
