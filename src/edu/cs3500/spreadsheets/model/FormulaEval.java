package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.CellRange;
import edu.cs3500.spreadsheets.model.formula.CellRef;
import edu.cs3500.spreadsheets.model.formula.FormulaVisitor;
import edu.cs3500.spreadsheets.model.formula.Func;
import edu.cs3500.spreadsheets.model.value.Bool;
import edu.cs3500.spreadsheets.model.value.ListValue;
import edu.cs3500.spreadsheets.model.value.Num;
import edu.cs3500.spreadsheets.model.value.Str;
import edu.cs3500.spreadsheets.model.value.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormulaEval implements FormulaVisitor<Value> {
  private final SpreadsheetModel model;

  public FormulaEval(SpreadsheetModel model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public Value visitBool(Bool b) {
    return b;
  }

  @Override
  public Value visitNum(Num n) {
    return n;
  }

  @Override
  public Value visitStr(Str s) {
    return s;
  }

  @Override
  public Value visitCellRef(CellRef c) {
    return model.evaluate(c.getRef());
  }

  @Override
  public Value visitCellRange(CellRange r) {
    List<Value> results = new ArrayList<>();
    for (Coord c : r.getCoords()) {
      results.add(model.evaluate(c));
    }
    return new ListValue(results);
  }

  @Override
  public Value visitFuncApp(Func f) {
    List<Value> argValues = f.getArgs().stream()
        .map(arg -> arg.accept(this))
        .toList();

    String funcName = f.getFuncName();
    switch (funcName) {
      case "SUM":
        return evalSum(argValues);
      default:
        throw new IllegalArgumentException("Unknown function name: " + funcName);
    }
  }

  @Override
  public Value visitListValue(ListValue l) {
    return l;
  }

  private Value evalSum(List<Value> args) {
    double sum = 0;
    boolean hasNumeric = false;
    for (Value v : args) {
      if (v instanceof Num) {
        sum += ((Num) v).getValue();
        hasNumeric = true;
      }
    }
    return new Num(sum);  // 无数值参数时，默认返回 0
  }
}
