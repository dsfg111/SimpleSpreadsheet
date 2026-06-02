package edu.cs3500.spreadsheets.model.value;

import java.math.BigDecimal;
import java.util.Objects;

public class NumValue implements Value{
  private final double val;

  public NumValue(double val) {
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
    NumValue that = (NumValue) o;
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
}
