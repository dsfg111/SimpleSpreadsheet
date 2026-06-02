package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.value.*;
import edu.cs3500.spreadsheets.sexp.*;
import java.util.*;
import java.util.function.Function;

public class EvalVisitor implements SexpVisitor<Value> {
  SpreadsheetModel model;

  EvalVisitor(SpreadsheetModel model) {
    this.model = Objects.requireNonNull(model);
  }


  @Override
  public Value visitBoolean(boolean b) {
    return new BoolValue(b);
  }

  @Override
  public Value visitNumber(double d) {
    return new NumValue(d);
  }

  @Override
  public Value visitSList(List<Sexp> l) {
    if (l.isEmpty()) {
      throw new IllegalArgumentException("Empty list");
    }

    Sexp first = l.get(0);
    String funcName = first.accept(new FunctionNameExtractor());

    List<Value> args = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      args.add(l.get(i).accept(this));
    }

    return executeFunction(funcName, args);
  }

  private static class FunctionNameExtractor implements SexpVisitor<String> {

    @Override
    public String visitBoolean(boolean b) {
      throw new IllegalArgumentException("Expected function name");
    }

    @Override
    public String visitNumber(double d) {

      throw new IllegalArgumentException("Expected function name");
    }

    @Override
    public String visitSList(List<Sexp> l) {

      throw new IllegalArgumentException("Expected function name");
    }

    @Override
    public String visitSymbol(String s) {
     return s;
    }

    @Override
    public String visitString(String s) {
      throw new IllegalArgumentException("Expected function name");
    }
  }

  private Value executeFunction(String funcName, List<Value> args) {
    switch (funcName) {
      case "SUM":
        return computeSum(args);
      default:
        throw new IllegalArgumentException("Unknown function: " + funcName);
    }
  }

  private Value computeSum(List<Value> args) {
    SumAccumulator acc = new SumAccumulator();
    for (Value arg : args) {
      arg.accept(acc);
    }
    return new NumValue(acc.getResult());
  }

  private static class SumAccumulator implements ValueVisitor<Double> {
    private double sum = 0;
    private boolean hasNumeric = false;

    @Override
    public Double visitNum(double d) {
      sum += d;
      hasNumeric = true;
      return sum;
    }

    @Override
    public Double visitBool(boolean b) {
      return sum;
    }

    @Override
    public Double visitStr(String s) {
      return sum;
    }

    @Override
    public Double visitRegion(List<Value> cells) {
      for (Value v : cells) {
        v.accept(this);
      }
      return sum;
    }

    public double getResult() {
      return hasNumeric ? sum : 0;
    }
  }

  @Override
  public Value visitSymbol(String s) {
    // 引用区域
    if (s.contains(":")) {
      String[] parts = s.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid range reference");
      }
      Coord start = Coord.parseCoord(parts[0]);
      Coord end = Coord.parseCoord(parts[1]);

      return model.getRegionValue(start, end);
    }
    // 单个单元格引用
    if (Coord.isValidCoordName(s)) {    // 是有效坐标
      Coord coord = Coord.parseCoord(s);
      return model.evaluate(coord);
    }

    throw new IllegalArgumentException("Unknown symbol");
  }

  @Override
  public Value visitString(String s) {
    return new StrValue(s);
  }
}
