package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.value.*;
import edu.cs3500.spreadsheets.model.formula.*;
import edu.cs3500.spreadsheets.sexp.*;
import java.util.*;

public class BasicSpreadsheetModel implements SpreadsheetModel{
  // 存储单个单元格的完整状态
  private static class CellData {
    String rawContents;   // 原始字符串
    Sexp sexp;    // 原始字符串解析出的 S 表达式
    Formula formula;  // Sexp 转换的公式

    CellData(String raw, Sexp sexp, Formula formula) {
      this.rawContents = raw;
      this.sexp = sexp;
      this.formula = formula;
    }
  }

  // 所有非空有效单元格
  private Map<Coord, CellData> cells = new HashMap<>();
  // 求值访问器
  FormulaVisitor<Value> evalFormula = new FormulaEval(this);
  // Sexp 转 Formula 访问器
  SexpVisitor<Formula> formulaTrans = new FormulaTranslator();

  @Override
  public void setCellContents(Coord loc, String contents) {
    if (loc == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    if (contents == null) {
      throw new IllegalArgumentException("Contents cannot be null");
    }
    // 内容为空白，则清空单元格数据
    if (contents.isBlank()) {
      cells.remove(loc);
      return;
    }

    Sexp sexp = Parser.parse(contents);
    Formula formula = sexp.accept(formulaTrans);
    CellData cellData = new CellData(contents, sexp, formula);

    cells.put(loc, cellData);
  }

  @Override
  public String getCellContents(Coord loc) {
    if (loc == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }

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
      Formula formula = data.formula;

      return formula.accept(evalFormula);
    } else {    // null 代表空值
      return null;
    }
  }
}
