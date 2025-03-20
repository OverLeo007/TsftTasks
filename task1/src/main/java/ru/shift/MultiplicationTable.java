package ru.shift;

import java.util.stream.IntStream;

public class MultiplicationTable {
  private static final String VERTICAL_DELIMITER = "|";
  private static final String HORIZONTAL_DELIMITER = "-";
  private static final String CROSS_DELIMITER = "+";
  private static final String SPACE = " ";
  private static final String EOL = System.lineSeparator();

  private final int tableSize;
  private final int maxCellLen;
  private final int maxCellLenFirstCol;
  private final String firstSpace;
  private final String dividerLine;
  private final int lineLenWithDividerLine;
  private final String firstColCellFormat;
  private final String lineWithoutFirstColFormat;

  public MultiplicationTable(Integer tableSize) {
    this.tableSize = tableSize;

    this.maxCellLen = String.valueOf(tableSize * tableSize).length();
    this.maxCellLenFirstCol = String.valueOf(tableSize).length();

    this.dividerLine = getDividerLine();
    this.firstSpace = SPACE.repeat(maxCellLenFirstCol);

    /* tableSize для VERTICAL_DELIMITER, 1 для EOL */
    this.lineLenWithDividerLine =
        maxCellLenFirstCol + maxCellLen * tableSize + tableSize + dividerLine.length() + 1;

    this.firstColCellFormat = "%" + (maxCellLenFirstCol > 1 ? maxCellLenFirstCol + "d" : "d");
    String cellFormat = "%" + maxCellLen + "d";
    this.lineWithoutFirstColFormat =
        (cellFormat + VERTICAL_DELIMITER).repeat(tableSize - 1) + cellFormat;
  }

  public void printTable() {
    IoUtils.print(getFirstLine());
    for (var i = 1; i < tableSize + 1; i++) {
      IoUtils.print(getNumbersLine(i));
    }
  }

  private String getFirstLine() {
    return buildLine(1, true);
  }

  private String getNumbersLine(int rowNumber) {
    return buildLine(rowNumber, false);
  }

  private String buildLine(int firstValue, boolean isHeader) {
    StringBuilder line = new StringBuilder(lineLenWithDividerLine);

    /* Считаем заранее значения для строки */
    Object[] values =
        IntStream.range(1, tableSize + 1).map(i -> firstValue * i).boxed().toArray(Object[]::new);

    /* Так как первый элемент не является частью вычисляемых значений
    (а является номером строки) - форматируется отдельно */
    line.append(isHeader ? firstSpace : String.format(firstColCellFormat, firstValue));
    line.append(VERTICAL_DELIMITER);

    line.append(lineWithoutFirstColFormat);

    line.append(EOL);
    line.append(dividerLine);

    return String.format(line.toString(), values);
  }

  private String getDividerLine() {
    String underNumber = HORIZONTAL_DELIMITER.repeat(maxCellLen);
    return HORIZONTAL_DELIMITER.repeat(maxCellLenFirstCol)
        + CROSS_DELIMITER
        + (underNumber + CROSS_DELIMITER).repeat(tableSize - 1)
        + underNumber;
  }
}
