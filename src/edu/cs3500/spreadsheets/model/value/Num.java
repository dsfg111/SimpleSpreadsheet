package edu.cs3500.spreadsheets.model.value;

import edu.cs3500.spreadsheets.model.formula.FormulaVisitor;
import java.math.BigDecimal;

public class Num implements Value{
  private final double val;

  public Num(double val) {
    this.val = val;
  }

  public double getValue() {
    return val;
  }

  @Override
  public String toString() {
    return BigDecimal.valueOf(val).stripTrailingZeros().toPlainString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Num that = (Num) o;
    return Double.compare(val, that.val) == 0;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(val);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitNum(val);
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitNum(this);
  }
}
