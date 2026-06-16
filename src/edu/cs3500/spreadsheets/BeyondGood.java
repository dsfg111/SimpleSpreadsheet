package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.model.BasicWorksheetBuilder;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SpreadsheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.value.Value;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    /*
      TODO: For now, look in the args array to obtain a filename and a cell name,
      - read the file and build a model from it, 
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */
    BasicWorksheetBuilder builder = new BasicWorksheetBuilder();

    Scanner sc = new Scanner(System.in);
    while (sc.hasNext()) {
      String in = sc.next();
      switch (in) {
        case "quit":
        case "q":
          return;
        case "-in":
          String filename = sc.next();
          String str = sc.next();
          if (!str.equals("-eval")) {
            System.out.println("Not match -eval | " + str);
            break;
          }
          String cellname = sc.next();
          if (!(Coord.isValidCoordName(cellname))) {
            System.out.println("Invalid cellname " + cellname);
            break;
          }
          try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            SpreadsheetModel model = WorksheetReader.read(builder, reader);
            Coord coord = Coord.parseCoord(cellname);
            Value result = model.evaluate(coord);
            System.out.println(result);
          } catch (IOException e) {
            System.out.println("Can not find file " + filename);
          } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
          }
          break;
        default:
          System.out.println("Please input a valid command: -in some-filename -eval some-cellname");
          break;
      }
    }
  }
}
