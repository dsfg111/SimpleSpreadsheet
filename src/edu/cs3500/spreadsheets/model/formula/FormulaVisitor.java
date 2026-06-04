package edu.cs3500.spreadsheets.model.formula;

import edu.cs3500.spreadsheets.model.value.*;

public interface FormulaVisitor<R> {
  R visitBool(Bool b);
  R visitNum(Num n);
  R visitStr(Str s);
  R visitCellRef(CellRef c);
  R visitCellRange(CellRange r);
  R visitFuncApp(Func f);
  R visitListValue(ListValue l);
}
