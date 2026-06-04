package edu.cs3500.spreadsheets.model.value;

import edu.cs3500.spreadsheets.model.formula.FormulaVisitor;
import java.util.Objects;

public class Bool implements Value{
  private final boolean val;

  public Bool(boolean val) {
    this.val = val;
  }

  public boolean getValue() {
    return val;
  }

  @Override
  public String toString() {
    return String.valueOf(val);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bool that = (Bool) o;
    return val == that.val;
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitBool(val);
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitBool(this);
  }
}
