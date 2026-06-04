package edu.cs3500.spreadsheets.model.value;


import edu.cs3500.spreadsheets.model.formula.Formula;

/**
 * 代表单元格最终计算出的值
 */
public interface Value extends Formula {
  <R> R accept(ValueVisitor<R> visitor);
}
