package edu.cs3500.spreadsheets.model.formula;

import edu.cs3500.spreadsheets.model.Coord;
import java.util.ArrayList;
import java.util.List;

/**
 * 矩形区域引用，如 "A1:B2"
 * 第二个坐标的行列不应小于第一个坐标
 */
public class CellRange implements Formula{
  private final Coord start, end;

  public CellRange(Coord start, Coord end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Start or end cannot be null");
    }
    if (start.col > end.col) {
      throw new IllegalArgumentException("End col must >= start col");
    }
    if (start.row > end.row) {
      throw new IllegalArgumentException("End row must >= start row");
    }

    this.start = start;
    this.end = end;
  }

  public List<Coord> getCoords() {
    List<Coord> result = new ArrayList<>();
    for (int col = start.col; col <= end.col; col++) {
      for (int row = start.row; row <= end.row; row++) {
        result.add(new Coord(col, row));
      }
    }
    return result;
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitCellRange(this);
  }
}
