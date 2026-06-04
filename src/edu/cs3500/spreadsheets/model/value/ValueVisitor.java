package edu.cs3500.spreadsheets.model.value;

import java.util.List;

public interface ValueVisitor<R> {
  R visitNum(double d);
  R visitBool(boolean b);
  R visitStr(String s);
  R visitListValue(List<Value> values);
}
