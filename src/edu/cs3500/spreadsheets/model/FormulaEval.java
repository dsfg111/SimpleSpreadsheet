package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.*;
import edu.cs3500.spreadsheets.model.value.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
  public Value visitListValue(ListValue l) {
    return l;
  }

  @Override
  public Value visitBlank(Blank blank) {
    return blank;
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
    // 将所有矩形区域引用的 ListValue 展开
    List<Value> argValues = f.getArgs().stream()
        .map(arg -> arg.accept(this))
        .toList();

    String funcName = f.getFuncName();
    switch (funcName) {
      case "SUM":
        return evalSum(argValues);
      case "PRODUCT":
        return evalProduct(argValues);
      case "<":
        return evalLessThan(argValues);
      case "CONCAT":
        return evalConcat(argValues);
      default:
        throw new IllegalArgumentException("Unknown function name: " + funcName);
    }
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

  private Value evalProduct(List<Value> args) {
    double acc = 1;
    boolean hasNumeric = false;
    for (Value v : args) {
      if (v instanceof Num) {
        acc *= ((Num) v).getValue();
        hasNumeric = true;
      }
    }
    return hasNumeric ? new Num(acc) : new Num(0);  // 无数值参数时，默认返回 0
  }

  private Value evalLessThan(List<Value> args) {
    if (args.size() != 2) {
      throw new IllegalArgumentException("< requires exactly 2 arguments");
    }
    Value left = args.get(0);
    Value right = args.get(1);
    if (!(left instanceof Num) || !(right instanceof Num)) {
      throw new IllegalArgumentException("< requires numeric arguments");
    }
    boolean ans = ((Num) left).getValue() < ((Num) right).getValue();
    return new Bool(ans);
  }

  private Value evalConcat(List<Value> args) {
    StringBuilder sb = new StringBuilder();
    for (Value v : args) {
      if (v instanceof Str) {
        sb.append(((Str) v).getValue());
      }
    }
    return new Str(sb.toString());
  }
}
