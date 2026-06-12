package edu.cs3500.spreadsheets.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.cs3500.spreadsheets.model.value.Bool;
import edu.cs3500.spreadsheets.model.value.ListValue;
import edu.cs3500.spreadsheets.model.value.Num;
import edu.cs3500.spreadsheets.model.value.Str;
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

    // 设置 A1 为 "true"
    // 设置 A2 为 "A1"
    model.setCellContents(A1, "true");
    model.setCellContents(A2, "A1");
    assertEquals("true", model.getCellContents(A1));
    assertEquals("A1", model.getCellContents(A2));
  }

  @Test
  void testSetCellContent_unknownSymbol() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    // 设置 A2 为 "ABC"
    assertThrows(
        IllegalArgumentException.class,
        () -> model.setCellContents(A1, "ABC")
    );
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
    assertEquals(new Num(1), v1);
    assertEquals(new Num(3.14), v2);
    assertEquals("1", v1.toString());
    assertEquals("3.14", v2.toString());

    model.setCellContents(A1, "9.1");
    assertEquals(new Num(9.1), model.evaluate(A1));
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
    assertEquals(new Bool(true), v1);
    assertEquals(new Bool(false), v2);
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
    assertEquals(new Str("hello"), v1);
    assertEquals(new Str("false"), v2);
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

    assertEquals(new Num(1), model.evaluate(A1));
    assertEquals(new Num(1), model.evaluate(A2));

    model.setCellContents(A2, "B1");
    assertEquals(Blank.INSTANCE, model.evaluate(A2));
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

    assertEquals(new Num(2), model.evaluate(A3));

    Coord B1 = new Coord(2, 1);
    model.setCellContents(B1, "(SUM A3 1)");
    assertEquals(new Num(3), model.evaluate(B1));
  }

  @Test
  void testEvaluate_funcSum_notNumArgs() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(SUM true false)");
    assertEquals(new Num(0), model.evaluate(A1));

    model.setCellContents(A1, "(SUM \"NotNum\")");
    assertEquals(new Num(0), model.evaluate(A1));

    model.setCellContents(A1, "(SUM 1 \"NotNum\")");
    assertEquals(new Num(1), model.evaluate(A1));
  }

  @Test
  void testEvaluate_emptyCell() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    assertEquals(Blank.INSTANCE, model.evaluate(A1));
  }

  @Test
  void testEvaluate_funcProduct() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(PRODUCT 1)");
    assertEquals(new Num(1), model.evaluate(A1));

    model.setCellContents(A1, "(PRODUCT 1 2)");
    assertEquals(new Num(2), model.evaluate(A1));

    model.setCellContents(A1, "(PRODUCT 10 11)");
    assertEquals(new Num(110), model.evaluate(A1));
  }

  @Test
  void testEvaluate_funcProduct_notNumArgs() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(PRODUCT true false)");
    assertEquals(new Num(0), model.evaluate(A1));

    model.setCellContents(A1, "(PRODUCT \"NotNum\")");
    assertEquals(new Num(0), model.evaluate(A1));

    model.setCellContents(A1, "(PRODUCT 1 \"NotNum\")");
    assertEquals(new Num(1), model.evaluate(A1));
  }

  @Test
  void testEvaluate_funcLessThan() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(< 7 1)");
    assertEquals(new Bool(false), model.evaluate(A1));

    model.setCellContents(A1, "(< 7 9)");
    assertEquals(new Bool(true), model.evaluate(A1));

  }

  @Test
  void testEvaluate_funcLessThan_overTwoArg() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(< 7 9 2)");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );

    model.setCellContents(A1, "(< 7)");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );
  }


  @Test
  void testEvaluate_funcLessThan_notNumArg() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(< 7 true)");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );

    model.setCellContents(A1, "(< \"a\" \"b\")");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );
  }

  @Test
  void testEvaluate_funcConcat() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(CONCAT \"hello\")");
    assertEquals(new Str("hello"), model.evaluate(A1));

    model.setCellContents(A1, "(CONCAT \"hello \" \"world\")");
    assertEquals(new Str("hello world"), model.evaluate(A1));

  }


  @Test
  void testEvaluate_funcConcat_notStrArg() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "(CONCAT 1 2)");
    assertEquals(new Str(""), model.evaluate(A1));

    model.setCellContents(A1, "(CONCAT 9 \"hello\")");
    assertEquals(new Str("hello"), model.evaluate(A1));
  }

  @Test
  void testCyclicReference_directCellRef() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "A1");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );

    model.setCellContents(A1, "(SUM 1 A1)");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );
  }

  @Test
  void testCyclicReference_directCellRange() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);

    model.setCellContents(A1, "A1:B2");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );

    model.setCellContents(A1, "(SUM A1:B2)");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );
  }

  @Test
  void testEvaluate_CellRange() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);
    Coord A3 = new Coord(1, 3);
    Coord B1 = new Coord(2, 1);

    model.setCellContents(B1, "A1:A2");

    Value v = model.evaluate(B1);
    assertEquals(
        ListValue.class,
        v.getClass()
    );
  }

  @Test
  void testFuncSum_cellRange() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord A2 = new Coord(1, 2);
    Coord B1 = new Coord(2, 1);
    Coord B2 = new Coord(2, 2);
    Coord C1 = new Coord(3, 2);

    model.setCellContents(A1, "1");
    model.setCellContents(A2, "true");
    model.setCellContents(B1, "\"aaa\"");
    model.setCellContents(C1, "(SUM A1:B2)");

    assertEquals(
        new Num(1),
        model.evaluate(C1)
    );
  }


  @Test
  void testCyclicReference_indirectCellRef() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord B1 = new Coord(2, 1);

    model.setCellContents(A1, "B1");
    model.setCellContents(B1, "A1");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );

    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(B1)
    );
  }

  @Test
  void testCyclicReference_indirectCellRangeRef() {
    SpreadsheetModel model = new BasicSpreadsheetModel();
    Coord A1 = new Coord(1, 1);
    Coord B1 = new Coord(2, 1);

    model.setCellContents(A1, "B1");
    model.setCellContents(B1, "A1:A2");
    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(A1)
    );

    assertThrows(
        IllegalStateException.class,
        () -> model.evaluate(B1)
    );
  }
}