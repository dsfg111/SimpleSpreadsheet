package edu.cs3500.spreadsheets.sexp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParserTest {
  @Test
  void testParse_boolean() {
    Sexp s1 = Parser.parse("true");
    Sexp s2 = Parser.parse("false");

    assertEquals(SBoolean.class, s1.getClass());
    assertEquals(SBoolean.class, s2.getClass());

    assertEquals("true", s1.toString());
    assertEquals("false", s2.toString());
  }

  @Test
  void testParse_number() {
    Sexp s1 = Parser.parse("1");
    Sexp s2 = Parser.parse("3.14");

    assertEquals(SNumber.class, s1.getClass());
    assertEquals(SNumber.class, s2.getClass());

    assertEquals("1.0", s1.toString());
    assertEquals("3.14", s2.toString());
  }


  @Test
  void testParse_string() {
    Sexp s1 = Parser.parse("\"hello\"");
    Sexp s2 = Parser.parse("\"3.14\"");
    Sexp s3 = Parser.parse("\"true\"");

    assertEquals(SString.class, s1.getClass());
    assertEquals(SString.class, s2.getClass());
    assertEquals(SString.class, s3.getClass());

    assertEquals("\"hello\"", s1.toString());
    assertEquals("\"3.14\"", s2.toString());
    assertEquals("\"true\"", s3.toString());
  }

  @Test
  void testParse_symbol() {
    Sexp s1 = Parser.parse("A1");
    Sexp s2 = Parser.parse("S");
    Sexp s3 = Parser.parse("hello");
    Sexp s4 = Parser.parse("A2:B2");

    assertEquals(SSymbol.class, s1.getClass());
    assertEquals(SSymbol.class, s2.getClass());
    assertEquals(SSymbol.class, s3.getClass());
    assertEquals(SSymbol.class, s4.getClass());

    assertEquals("A1", s1.toString());
    assertEquals("S", s2.toString());
    assertEquals("hello", s3.toString());
    assertEquals("A2:B2", s4.toString());
  }

  @Test
  void testParse_list() {
    Sexp s1 = Parser.parse("(SUM A2:B2)");

    assertEquals(SList.class, s1.getClass());

    assertEquals("(SUM A2:B2)", s1.toString());
  }

  @Test
  void testParse_empty() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Parser.parse("")
    );
  }

  @Test
  void testParse_lackLeftQuote() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Parser.parse("hello\"")
    );
  }

  @Test
  void testParse_lackRightQuote() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Parser.parse("hello\"")
    );
  }

  @Test
  void testParse_lackLeftBracket() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Parser.parse("hello)")
    );
  }

  @Test
  void testParse_lackRightBracket() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Parser.parse("(hello")
    );
  }
}