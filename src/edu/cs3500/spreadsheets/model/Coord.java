package edu.cs3500.spreadsheets.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A value type representing coordinates in a {@link Worksheet}.
 */
public class Coord {
  public final int row;
  public final int col;

  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  private static final Pattern COORD_PATTERN = Pattern.compile("^([A-Z]+)([1-9][0-9]*)$");

  /**
   * 判断给定字符串是否是正确的电子表格单元格坐标索引的格式
   *
   * @param name 给定的坐标字符串
   * @return true,如果坐标格式正确
   */
  public static boolean isValidCoordName(String name) {
    if (name == null) return false;
    Matcher m = COORD_PATTERN.matcher(name);

    return m.matches();
  }

  /**
   * 从字符串构造坐标
   *
   * @param name 坐标字符串
   */
  public static Coord parseCoord(String name) {
    if (!isValidCoordName(name)) {
      throw new IllegalArgumentException("Coord name not right");
    }
    Matcher m = COORD_PATTERN.matcher(name);
    if (!m.matches()) {
      throw new IllegalStateException("Unexpected match failure for validated coord");
    }
    String colName = m.group(1);
    String rowName = m.group(2);

    int col = colNameToIndex(colName);
    int row = Integer.parseInt(rowName);

    return new Coord(col, row);
  }


  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
        && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}
