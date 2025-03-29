package ru.shift;


/**
 * Program that prints a multiplication table of the specified size.
 *
 * @author Lev Sokolov
 * @version 1.5
 * @since 2025.14.03
 */
public class Main {

  public static void main(String[] args) {
    int tableSize = IoUtils.scanInt();
    var table = new MultiplicationTable(tableSize);
    table.printTable();
  }
}
