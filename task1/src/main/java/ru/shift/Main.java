package ru.shift;

import java.util.Scanner;

/**
 * Program that prints a multiplication table of the specified size.
 *
 * @author Lev Sokolov
 * @version 1.0
 * @since 2025.14.03
 */
public class Main {
  private static final String VERTICAL_DELIMITER = "|";
  private static final String HORIZONTAL_DELIMITER = "-";
  private static final String CROSS_DELIMITER = "+";
  private static final String SPACE = " ";

  /** Entry point of the program. */
  public static void main(String[] args) {
    int tableSize = scanInt();
    printTable(tableSize);
  }

  /**
   * Prints the multiplication table of the specified size.
   *
   * @param tableSize size of the table
   */
  private static void printTable(int tableSize) {
    int maxCellLen = String.valueOf(tableSize * tableSize).length();
    int maxCellLenFirstCol = String.valueOf(tableSize).length();
    String dividerLine = getDividerLine(tableSize, maxCellLen, maxCellLenFirstCol);

    System.out.println(getFirstLine(tableSize, maxCellLen, maxCellLenFirstCol));
    System.out.println(dividerLine);

    for (var i = 1; i < tableSize + 1; i++) {
      System.out.println(getNumbersLine(i, tableSize, maxCellLen, maxCellLenFirstCol));
      System.out.println(dividerLine);
    }
  }

  /**
   * Returns the first line of the multiplication table. using the {@link #buildLine} method.
   *
   * @param tableSize size of the table
   * @param maxCellLen max length of the cell not in first column
   * @param maxCellLenFirstCol max length of the cell in first column
   * @return the first line of the multiplication table
   */
  private static String getFirstLine(int tableSize, int maxCellLen, int maxCellLenFirstCol) {
    return buildLine(1, tableSize, maxCellLen, maxCellLenFirstCol, true);
  }

  /**
   * Returns the not first line with numbers of the multiplication table using the {@link
   * #buildLine} method.
   *
   * @param rowNumber number of the row of the table
   * @param tableSize size of the table
   * @param maxCellLen max length of the cell not in first column
   * @param maxCellLenFirstCol max length of the cell in first column
   * @return the not first line with numbers of the multiplication table
   */
  private static String getNumbersLine(
      int rowNumber, int tableSize, int maxCellLen, int maxCellLenFirstCol) {
    return buildLine(rowNumber, tableSize, maxCellLen, maxCellLenFirstCol, false);
  }

  /**
   * Returns the divider line of the multiplication table.
   *
   * @param tableSize size of the table
   * @param maxCellLen max length of the cell not in first column
   * @param maxCellLenFirstCol max length of the cell in first column
   * @return the divider line of the multiplication table
   */
  private static String getDividerLine(int tableSize, int maxCellLen, int maxCellLenFirstCol) {
    String underNumber = HORIZONTAL_DELIMITER.repeat(maxCellLen);
    return HORIZONTAL_DELIMITER.repeat(maxCellLenFirstCol)
        + CROSS_DELIMITER
        + (underNumber + CROSS_DELIMITER).repeat(tableSize - 1)
        + underNumber;
  }

  /**
   * Returns custom the line of the multiplication table. Support for the first line and not first
   * line.
   *
   * @param firstValue first number of the line
   * @param tableSize size of the table
   * @param maxCellLen max length of the cell not in first column
   * @param maxCellLenFirstCol max length of the cell in first column
   * @param isHeader true if the line is the first line of the table
   * @return the custom line of the multiplication table
   */
  private static String buildLine(
      int firstValue, int tableSize, int maxCellLen, int maxCellLenFirstCol, boolean isHeader) {
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

  /**
   * Returns a cell with a number padded with spaces on the left according to cellLen.
   *
   * @param cellLen length of the cell
   * @param number number to be placed in the cell
   * @return the space padded cell with a number
   */
  private static String getCellWithNum(int cellLen, int number) {
    return String.format("%" + cellLen + "d", number);
  }

  /**
   * Scans an integer value from the console safely for incorrect input.
   *
   * @return the integer from the console
   */
  private static int scanInt() {
    System.out.println("Enter the number of rows from 1 to 32:");
    Scanner sc = new Scanner(System.in);
    int value;
    while (true) {
      String input = sc.nextLine();
      try {
        value = Integer.parseInt(input.trim());
        if (value >= 1 && value <= 32) {
          break;
        } else {
          System.out.println("Invalid input. Please enter an integer from 1 to 32.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter an integer from 1 to 32.");
      }
    }
    return value;
  }
}
