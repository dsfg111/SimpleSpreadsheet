package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.value.RegionValue;
import edu.cs3500.spreadsheets.model.value.Value;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.*;

public class BasicSpreadsheetModel implements SpreadsheetModel{
  // 存储单个单元格的完整状态
  private static class CellData {
    String rawContents;   // 原始字符串
    Sexp sexp;    // 原始字符串解析出的 s表达式

    CellData(String raw, Sexp sexp) {
      this.rawContents = raw;
      this.sexp = sexp;
    }
  }

  // 所有非空有效单元格
  private Map<Coord, CellData> cells = new HashMap<>();
  // 求值访问器
  SexpVisitor<Value> visitor = new EvalVisitor(this);

  @Override
  public void setCellContents(Coord loc, String contents) {
    Sexp sexp = Parser.parse(contents);
    CellData cellData = new CellData(contents, sexp);

    cells.put(loc, cellData);
  }

  @Override
  public String getCellContents(Coord loc) {
    if (cells.containsKey(loc)) {      // 非空单元格
      return cells.get(loc).rawContents;
    } else {    // 空单元格，返回空字符串
      return "";
    }
  }

  @Override
  public Value evaluate(Coord coord) throws IllegalStateException {
    if (cells.containsKey(coord)) {
      CellData data = cells.get(coord);
      Sexp sexp = data.sexp;

      return sexp.accept(visitor);
    } else {    // null 代表空值
      return null;
    }

  }

  @Override
  public Value getRegionValue(Coord start, Coord end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Start or end cannot be null");
    }
    if (start.col > end.col) {
      throw new IllegalArgumentException("End col must >= start col");
    }
    if (start.row > end.row) {
      throw new IllegalArgumentException("End row must >= start row");
    }

    List<Value> values = new ArrayList<>();
    for (int c = start.col; c <= end.col; c++) {
      for (int r = start.row; r <= end.row; r++) {
        Coord coord = new Coord(c, r);
        values.add(evaluate(coord));
      }
    }

    return new RegionValue(values);
  }

}
