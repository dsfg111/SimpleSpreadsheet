package edu.cs3500.spreadsheets.model.value;

import edu.cs3500.spreadsheets.model.formula.FormulaVisitor;
import java.util.Objects;

public class Str implements Value{
  private final String val;

  public Str(String val) {
    this.val = Objects.requireNonNull(val);
  }

  public String getValue() {
    return val;
  }

  @Override
  public String toString() {
    return "\"" + val + "\"";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Str that = (Str) o;
    return val.equals(that.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitStr(val);
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitStr(this);
  }
}
