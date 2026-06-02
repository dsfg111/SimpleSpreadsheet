package edu.cs3500.spreadsheets.model.value;

import edu.cs3500.spreadsheets.model.Coord;
import java.util.List;
import java.util.Objects;

/**
 * 对电子表格中矩形区域的引用,
 * 要求 end 坐标不小于 start 坐标
 */
public class RegionValue implements Value{
  private final List<Value> values;

  public RegionValue(List<Value> values) {
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
    RegionValue that = (RegionValue) o;
    return this.values.equals(that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values);
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitRegion(values);
  }
}
