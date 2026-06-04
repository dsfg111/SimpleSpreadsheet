package edu.cs3500.spreadsheets.model.formula;

import edu.cs3500.spreadsheets.model.Coord;
import java.util.Objects;

/**
 * 单个单元格引用
 */
public class CellRef implements Formula{
  private final Coord coord;

  public CellRef(Coord c) {
    this.coord = Objects.requireNonNull(c);
  }

  public Coord getRef() {
    return coord;
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitCellRef(this);
  }
}
