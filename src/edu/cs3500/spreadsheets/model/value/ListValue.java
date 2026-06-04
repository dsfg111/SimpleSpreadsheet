package edu.cs3500.spreadsheets.model.value;

import edu.cs3500.spreadsheets.model.formula.FormulaVisitor;
import java.util.List;
import java.util.Objects;

public class ListValue implements Value{
  private final List<Value> values;

  public ListValue(List<Value> values) {
    this.values = Objects.requireNonNull(values);
  }

  public List<Value> getValues() {
    return values;
  }

  @Override
  public String toString() {
    return values.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || o.getClass() != getClass()) return false;
    ListValue that = (ListValue) o;
    return this.values.equals(that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values);
  }


  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitListValue(values);
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitListValue(this);
  }
}
