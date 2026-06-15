package edu.cs3500.spreadsheets.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.cs3500.spreadsheets.model.value.*;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class WorksheetReaderTest {
  @Test
  void testRead() {
    BasicWorksheetBuilder builder = new BasicWorksheetBuilder();
    String text = """
        # Creates the four coordinates
        A1 3
        B1 4
        C1 9
        D1 12
        
        # Computes delta-x^2
        A2 =(PRODUCT (SUB C1 A1) (SUB C1 A1))
        # Computes delta-y^2
        B2 =(PRODUCT (SUB D1 B1) (SUB D1 B1))
        # Computes the rest of the distance formula
        A3 =(SQRT (SUM A2:B2))
        B3 =(< A3 10)
        
        # Computes the distance formula all in one step
        A4 =(SQRT (SUM (PRODUCT (SUB C1 A1) (SUB C1 A1)) (PRODUCT (SUB D1 B1) (SUB D1 B1))))
        """;
    Readable rd = new StringReader(text);
    SpreadsheetModel model = WorksheetReader.read(builder, rd);

    Coord A1 = new Coord(1, 1);
    Coord B1 = new Coord(2, 1);
    Coord C1 = new Coord(3, 1);
    Coord D1 = new Coord(4, 1);
    Coord A2 = new Coord(1, 2);
    Coord B2 = new Coord(2, 2);
    Coord A3 = new Coord(1, 3);
    Coord B3 = new Coord(2, 3);
    Coord A4 = new Coord(1, 4);

    assertEquals(new Num(3), model.evaluate(A1));
    assertEquals(new Num(4), model.evaluate(B1));
    assertEquals(new Num(9), model.evaluate(C1));
    assertEquals(new Num(12), model.evaluate(D1));
    assertEquals(new Num(36), model.evaluate(A2));
    assertEquals(new Num(64), model.evaluate(B2));
    assertEquals(new Num(10), model.evaluate(A3));
    assertEquals(new Bool(false), model.evaluate(B3));
    assertEquals(new Num(10), model.evaluate(A4));
  }
}