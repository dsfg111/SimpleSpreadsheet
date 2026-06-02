package edu.cs3500.spreadsheets.model.value;

/**
 * 代表单元格最终计算出的值
 */
public interface Value {
  <R> R accept(ValueVisitor<R> visitor);
}
