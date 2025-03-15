package ru.shift;


/**
 * Program that prints a multiplication table of the specified size.
 *
 * @author Lev Sokolov
 * @version 1.1
 * @since 2025.14.03
 */
public class Main {

  public static void main(String[] args) {
    int tableSize = IoManager.scanInt();
    var table = new MultiplicationTable(tableSize);
    table.printTable();
  }
}
