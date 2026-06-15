package edu.cs3500.spreadsheets.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.value.Num;
import org.junit.jupiter.api.Test;

class BasicWorksheetBuilderTest {
  @Test
  void testCreateCell() {
    BasicWorksheetBuilder builder = new BasicWorksheetBuilder();

    builder.createCell(1, 1, "1");
  }

  @Test
  void testCreateCell_errorCoord() {
    BasicWorksheetBuilder builder = new BasicWorksheetBuilder();

    assertThrows(
        IllegalArgumentException.class,
        () -> builder.createCell(0, 1, "1")
    );
  }

  @Test
  void testCreateCell_returnThis() {
    BasicWorksheetBuilder builder = new BasicWorksheetBuilder();

    WorksheetBuilder<SpreadsheetModel> ret = builder.createCell(1, 1, "1");

    assertEquals(ret, builder);
  }

  @Test
  void testCreateWorkSheet() {
    BasicWorksheetBuilder builder = new BasicWorksheetBuilder();

    builder.createCell(1, 1, "1");

    SpreadsheetModel model = builder.createWorksheet();
    assertEquals(new Num(1), model.evaluate(new Coord(1, 1)));

  }
}