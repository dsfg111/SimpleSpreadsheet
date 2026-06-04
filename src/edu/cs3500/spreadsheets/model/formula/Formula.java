package edu.cs3500.spreadsheets.model.formula;

public interface Formula {
  <R> R accept(FormulaVisitor<R> visitor);
}
