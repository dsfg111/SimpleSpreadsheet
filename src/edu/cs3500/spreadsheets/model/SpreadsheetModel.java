package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.value.Value;
import java.util.Map;
import java.util.Set;

/**
 * 电子表格模型
 */
public interface SpreadsheetModel {

  /**
   * 设置单元格的内容
   *
   * @param loc 单元格坐标
   * @param contents 原始字符串，如“3.14”，“=SUM(A1:B2)”，"HEllO"。
   * @throws IllegalArgumentException 如果参数为 null
   */
  void setCellContents(Coord loc, String contents);

  /**
   * 获取单元格的原始内容,如果为空则返回空字符串 ""
   *
   * @param loc 单元格位置
   * @return 原始字符串内容
   * @throws IllegalArgumentException 如果参数为 null
   */
  String getCellContents(Coord loc);

  /**
   * 获取单元格求值后的结果
   *
   * @param coord 单元格坐标
   * @return 单元格的值
   * @throws IllegalStateException 如果单元格无法求值
   */
  Value evaluate(Coord coord) throws IllegalStateException;

}
