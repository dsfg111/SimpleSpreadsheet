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
    Value cachedValue; // 缓存的值

    CellData(String raw, Sexp sexp, Formula formula) {
      this.rawContents = raw;
      this.sexp = sexp;
      this.formula = formula;
    }
  }

  // 所有非空有效单元格
  private Map<Coord, CellData> cells = new HashMap<>();
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
  public Value evaluate(Coord loc){
    if (loc == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }

    Set<Coord> visiting = new HashSet<>();
    Set<Coord> visited = new HashSet<>();

    return evaluateRecursive(loc, visiting, visited);
  }

  /**
   * 递归求值
   *
   * @param loc  求值单元格坐标
   * @param visiting 正在走的路径坐标
   * @param visited  已经求出结果的坐标，可直接取结果
   * @return
   */
  private Value evaluateRecursive(Coord loc, Set<Coord> visiting, Set<Coord> visited) {
    // 检查循环引用
    // 如果当前单元格已经在“正在计算”的集合中，说明形成了环
    if (visiting.contains(loc)) {
      throw new IllegalStateException("Circular reference detected at cell: " + loc);
    }

    CellData cell = cells.get(loc);
    if (cell == null) {
      return Blank.INSTANCE;
    }
    if (visited.contains(loc)) {
      return cell.cachedValue;
    }

    visiting.add(loc);

    try {
      FormulaVisitor<Value> evalVisitor = new FormulaEval(visiting, visited);
      Value result = cell.formula.accept(evalVisitor);
      cell.cachedValue = result;

      return result;
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException(
          "Error in cell " + loc + ": " + e.getMessage(), e
      );
    } finally {
      // 无论成功失败，都把当前节点从 visiting 中移除
      visiting.remove(loc);
      visited.add(loc);
    }
  }

  private class FormulaEval implements FormulaVisitor<Value> {
    private final Set<Coord> visiting;
    private final Set<Coord> visited;

    public FormulaEval(Set<Coord> visiting, Set<Coord> visited) {
      this.visiting = visiting;
      this.visited = visited;
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
      return evaluateRecursive(c.getRef(), visiting, visited);
    }

    @Override
    public Value visitCellRange(CellRange r) {
      List<Value> results = new ArrayList<>();
      for (Coord c : r.getCoords()) {
        results.add(evaluateRecursive(c, visiting, visited));
      }
      return new ListValue(results);
    }

    @Override
    public Value visitFunc(Func f) {
      // 将所有矩形区域引用的 ListValue 展开
      List<Value> argValues = f.getArgs().stream()
          .map(arg -> arg.accept(this))
          .toList();

      List<Value> flatArgs = flattenValues(argValues);

      String funcName = f.getFuncName();
      switch (funcName) {
        case "SUM":
          return evalSum(flatArgs);
        case "PRODUCT":
          return evalProduct(flatArgs);
        case "<":
          return evalLessThan(flatArgs);
        case "CONCAT":
          return evalConcat(flatArgs);
        case "SUB":
          return evalSub(flatArgs);
        case "SQRT":
          return evalSqrt(flatArgs);
        default:
          throw new IllegalArgumentException("Unknown function name: " + funcName);
      }
    }

    /**
     * 将所有可能嵌套 ListValue 的值列表展平为一维列表
     * 例如: [Num(1), ListValue([Num(2), Num(3)]), Num(4)]
     * 变为: [Num(1), Num(2), Num(3), Num(4)]
     */
    private List<Value> flattenValues(List<Value> values) {
      List<Value> flatList = new ArrayList<>();
      for (Value v : values) {
        if (v instanceof ListValue) {
          ListValue lst = (ListValue) v;
          flatList.addAll(flattenValues(lst.getValues()));
        } else {
          flatList.add(v);
        }
      }
      return flatList;
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

  private Value evalSub(List<Value> args) {
    if (args.size() != 2) {
      throw new IllegalArgumentException("SUB required exactly two arguments");
    }
    Value left = args.get(0);
    Value right = args.get(1);
    if (!(left instanceof Num) || !(right instanceof Num)) {
      throw new IllegalArgumentException("SUB requires numeric arguments");
    }
    double lv = ((Num) left).getValue();
    double rv = ((Num) right).getValue();
    return new Num(lv - rv);
  }

  private Value evalSqrt(List<Value> args) {
    if (args.size() != 1) {
      throw new IllegalArgumentException("SQRT required exactly one arguments");
    }
    Value v = args.get(0);
    if (!(v instanceof Num)) {
      throw new IllegalArgumentException("SQRT requires numeric arguments");
    }
    double n = ((Num) v).getValue();
    return new Num(Math.sqrt(n));
  }
}
