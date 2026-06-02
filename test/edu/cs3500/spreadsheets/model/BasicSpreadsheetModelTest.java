package edu.cs3500.spreadsheets.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.cs3500.spreadsheets.model.value.BoolValue;
import edu.cs3500.spreadsheets.model.value.NumValue;
import edu.cs3500.spreadsheets.model.value.StrValue;
import edu.cs3500.spreadsheets.model.value.Value;
import org.junit.jupiter.api.Test;

class BasicSpreadsheetModelTest {
  @Test
  void testSetAndGetCellContent() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);

    // 设置 A1 为 "3.14"
    model.setCellContents(A1, "3.14");
    assertEquals("3.14", model.getCellContents(A1));
    assertEquals("", model.getCellContents(A2));

    // 设置 A2 为 "ABC"
    model.setCellContents(A2, "ABC");
    assertEquals("3.14", model.getCellContents(A1));
    assertEquals("ABC", model.getCellContents(A2));

    // 设置 A1 为 "true"
    // 设置 A2 为 "A1"
    model.setCellContents(A1, "true");
    model.setCellContents(A2, "A1");
    assertEquals("true", model.getCellContents(A1));
    assertEquals("A1", model.getCellContents(A2));

  }

  @Test
  void testEvaluate_num() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);

    model.setCellContents(A1, "1");
    model.setCellContents(A2, "3.14");
    Value v1 = model.evaluate(A1);
    Value v2 = model.evaluate(A2);
    assertEquals(new NumValue(1), v1);
    assertEquals(new NumValue(3.14), v2);
    assertEquals("1", v1.toString());
    assertEquals("3.14", v2.toString());

    model.setCellContents(A1, "9.1");
    assertEquals(new NumValue(9.1), model.evaluate(A1));
  }

  @Test
  void testEvaluate_bool() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);

    model.setCellContents(A1, "true");
    model.setCellContents(A2, "false");
    Value v1 = model.evaluate(A1);
    Value v2 = model.evaluate(A2);
    assertEquals(new BoolValue(true), v1);
    assertEquals(new BoolValue(false), v2);
    assertEquals("true", v1.toString());
    assertEquals("false", v2.toString());
  }

  @Test
  void testEvaluate_str() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);

    model.setCellContents(A1, "\"hello\"");
    model.setCellContents(A2, "\"false\"");
    Value v1 = model.evaluate(A1);
    Value v2 = model.evaluate(A2);
    assertEquals(new StrValue("hello"), v1);
    assertEquals(new StrValue("false"), v2);
    assertEquals("\"hello\"", v1.toString());
    assertEquals("\"false\"", v2.toString());
  }

  @Test
  void testEvaluate_symbolCoord() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);

    model.setCellContents(A1, "1");
    model.setCellContents(A2, "A1");

    assertEquals(new NumValue(1), model.evaluate(A1));
    assertEquals(new NumValue(1), model.evaluate(A2));

    model.setCellContents(A2, "B1");
    assertNull(model.evaluate(A2));
  }

  @Test
  void testEvaluate_funcSum() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);
    Coord A3 = new Coord(1, 3);

    model.setCellContents(A1, "1");
    model.setCellContents(A2, "1");
    model.setCellContents(A3, "(SUM A1 A2)");

    assertEquals(new NumValue(2), model.evaluate(A3));

    Coord B1 = new Coord(2, 1);
    model.setCellContents(B1, "(SUM A3 1)");
    assertEquals(new NumValue(3), model.evaluate(B1));
  }
}