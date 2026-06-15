package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;

public class BasicWorksheetBuilder implements WorksheetBuilder<SpreadsheetModel> {
  private final BasicSpreadsheetModel model;

  public BasicWorksheetBuilder() {
    this.model = new BasicSpreadsheetModel();
  }

  @Override
  public WorksheetBuilder<SpreadsheetModel> createCell(int col, int row, String contents) {
    model.setCellContents(new Coord(col, row), contents);

    return this;
  }

  @Override
  public SpreadsheetModel createWorksheet() {
    return model;
  }
}
