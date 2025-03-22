package ru.shift;


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

  private final String cellFormat;

  public MultiplicationTable(Integer tableSize) {
    this.tableSize = tableSize;

    this.maxCellLen = String.valueOf(tableSize * tableSize).length();
    this.maxCellLenFirstCol = String.valueOf(tableSize).length();

    this.dividerLine = getDividerLine();

    this.firstSpace = SPACE.repeat(maxCellLenFirstCol);

    this.firstColCellFormat = "%" + (maxCellLenFirstCol > 1 ? maxCellLenFirstCol + "d" : "d");
    this.cellFormat = "%" + maxCellLen + "d";

    this.lineLenWithDividerLine =
        maxCellLenFirstCol
            + (Math.max(maxCellLen, cellFormat.length())) * tableSize // cells len
            + tableSize // VERTICAL_DELIMITER len
            + dividerLine.length() // divider line len
            + EOL.length() * 2; // EOL after cells + EOL after divider line
  }

  public void printTable() {
    IoUtils.print(getFirstLine());
    for (var i = 1; i < tableSize + 1; i++) {
      IoUtils.print(getNumbersLine(i));
    }
  }

  private String getFirstLine() {
    StringBuilder line = new StringBuilder(lineLenWithDividerLine);

    line.append(firstSpace);
    return buildEndOfLine(line, 1);
  }

  private String getNumbersLine(int rowNumber) {
    StringBuilder line = new StringBuilder(lineLenWithDividerLine);

    line.append(String.format(firstColCellFormat, rowNumber));
    return buildEndOfLine(line, rowNumber);
  }

  private String buildEndOfLine(StringBuilder line, int firstValue) {
    for(int i = 1; i < tableSize + 1; i++) {
      line.append(VERTICAL_DELIMITER);
      line.append(String.format(cellFormat, firstValue * i));
    }
    line.append(EOL).append(dividerLine).append(EOL);

    return line.toString();
  }

  private String getDividerLine() {
    String underNumber = HORIZONTAL_DELIMITER.repeat(maxCellLen);
    return HORIZONTAL_DELIMITER.repeat(maxCellLenFirstCol)
        + CROSS_DELIMITER
        + (underNumber + CROSS_DELIMITER).repeat(tableSize - 1)
        + underNumber;
  }
}
