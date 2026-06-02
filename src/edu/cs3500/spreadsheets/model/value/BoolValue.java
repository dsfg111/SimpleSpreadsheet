package edu.cs3500.spreadsheets.model.value;

import java.util.Objects;

public class BoolValue implements Value{
  private final boolean val;

  public BoolValue(boolean val) {
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
    BoolValue that = (BoolValue) o;
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
}
