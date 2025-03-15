package ru.shift;

public class MultiplicationTable {
  private static final String VERTICAL_DELIMITER = "|";
  private static final String HORIZONTAL_DELIMITER = "-";
  private static final String CROSS_DELIMITER = "+";
  private static final String SPACE = " ";

  private final int tableSize;
  private final int maxCellLen;
  private final int maxCellLenFirstCol;
  private final String dividerLine;

  public MultiplicationTable(Integer tableSize) {
    this.tableSize = tableSize;
    this.maxCellLen = String.valueOf(tableSize * tableSize).length();
    this.maxCellLenFirstCol = String.valueOf(tableSize).length();
    this.dividerLine = getDividerLine();
  }

  public void printTable() {
    IoManager.printLines(getFirstLine(), dividerLine);
    for (var i = 1; i < tableSize + 1; i++) {
      IoManager.printLines(getNumbersLine(i), dividerLine);
    }
  }

  private String getFirstLine() {
    return buildLine(1, true);
  }

  private String getNumbersLine(int rowNumber) {
    return buildLine(rowNumber, false);
  }

  private String buildLine(int firstValue, boolean isHeader) {
    StringBuilder line = new StringBuilder();

    line.append(
        isHeader
            ? SPACE.repeat(maxCellLenFirstCol)
            : getCellWithNum(maxCellLenFirstCol, firstValue));
    line.append(VERTICAL_DELIMITER);

    for (var i = 1; i < tableSize; i++) {
      int value = isHeader ? i : firstValue * i;
      line.append(getCellWithNum(maxCellLen, value));
      line.append(VERTICAL_DELIMITER);
    }

    String lastCell = getCellWithNum(maxCellLen, firstValue * tableSize);
    line.append(lastCell);
    return line.toString();
  }

  private static String getCellWithNum(int cellLen, int number) {
    return String.format("%" + cellLen + "d", number);
  }

  private String getDividerLine() {
    String underNumber = HORIZONTAL_DELIMITER.repeat(maxCellLen);
    return HORIZONTAL_DELIMITER.repeat(maxCellLenFirstCol)
        + CROSS_DELIMITER
        + (underNumber + CROSS_DELIMITER).repeat(tableSize - 1)
        + underNumber;
  }
}
