package edu.cs3500.spreadsheets.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CoordTest {
  @Test
  void testIsValidCoordName() {
    assertTrue(Coord.isValidCoordName("A1"));
    assertTrue(Coord.isValidCoordName("A11"));
    assertTrue(Coord.isValidCoordName("AZZ11"));
    assertFalse(Coord.isValidCoordName("A"));
    assertFalse(Coord.isValidCoordName("12"));
    assertFalse(Coord.isValidCoordName("a1"));
  }

  @Test
  void testParseCoord() {
    assertEquals(new Coord(1, 1), Coord.parseCoord("A1"));
    assertEquals(new Coord(26, 11), Coord.parseCoord("Z11"));
    assertEquals(new Coord(27, 1), Coord.parseCoord("AA1"));
    assertEquals(new Coord(52, 1), Coord.parseCoord("AZ1"));
    assertEquals(new Coord(78, 1), Coord.parseCoord("BZ1"));
    assertEquals(new Coord(703, 1), Coord.parseCoord("AAA1"));
  }

}