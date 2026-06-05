package edu.cs3500.spreadsheets.model;


import edu.cs3500.spreadsheets.model.formula.FormulaVisitor;
import edu.cs3500.spreadsheets.model.value.Value;
import edu.cs3500.spreadsheets.model.value.ValueVisitor;

/**
 * 空白单元格
 */
public enum Blank implements Value {
  INSTANCE;

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitBlank(this);
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitBlank(this);
  }
}
