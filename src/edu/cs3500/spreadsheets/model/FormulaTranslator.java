package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.*;
import edu.cs3500.spreadsheets.model.value.*;
import edu.cs3500.spreadsheets.sexp.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来将 {@link Sexp} 转换为 {@link Formula}
 */
public class FormulaTranslator implements SexpVisitor<Formula> {

  @Override
  public Formula visitBoolean(boolean b) {
    return new Bool(b);
  }

  @Override
  public Formula visitNumber(double d) {
    return new Num(d);
  }

  @Override
  public Formula visitSList(List<Sexp> l) {
    if (l.isEmpty()) {
      throw new IllegalArgumentException("List cannot be empty");
    }

    // 提取函数名
    Sexp head = l.get(0);
    if (!(head instanceof SSymbol)) {
      throw new IllegalArgumentException(
          "Expected function name as symbol, but got: " + head
      );
    }
    String funcName = ((SSymbol) head).getName();

    // 获取参数
    List<Formula> args = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      args.add(l.get(i).accept(this));
    }

    return new Func(funcName, args);
  }

  @Override
  public Formula visitSymbol(String s) {
    // 引用区域
    if (s.contains(":")) {
      String[] parts = s.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid range reference: " + s);
      }
      Coord start = Coord.parseCoord(parts[0]);
      Coord end = Coord.parseCoord(parts[1]);

      return new CellRange(start, end);
    }
    // 单个单元格引用
    if (Coord.isValidCoordName(s)) {    // 是有效坐标
      Coord coord = Coord.parseCoord(s);
      return new CellRef(coord);
    }

    throw new IllegalArgumentException("Unknown symbol: " + s);
  }

  @Override
  public Formula visitString(String s) {
    return new Str(s);
  }
}
