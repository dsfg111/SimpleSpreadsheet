package edu.cs3500.spreadsheets.model.formula;

import java.util.Collections;
import java.util.List;

/**
 * 将函数应用到实际的计算逻辑
 * e.g., (SUM A1 B2)
 */
public class Func implements Formula{
  private final String funcName;
  private final List<Formula> args;

  public Func(String funcName, List<Formula> args) {
    this.funcName = funcName;
    this.args = Collections.unmodifiableList(args);
  }

  public String getFuncName() {
    return funcName;
  }

  public List<Formula> getArgs() {
    return args;
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitFunc(this);
  }
}
