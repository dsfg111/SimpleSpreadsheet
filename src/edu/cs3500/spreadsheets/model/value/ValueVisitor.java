package edu.cs3500.spreadsheets.model.value;

import edu.cs3500.spreadsheets.model.Blank;
import java.util.List;

public interface ValueVisitor<R> {
  R visitNum(double d);
  R visitBool(boolean b);
  R visitStr(String s);
  R visitListValue(List<Value> values);
  R visitBlank(Blank blank);
}
