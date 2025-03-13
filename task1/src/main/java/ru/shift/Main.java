package ru.shift;

import java.util.Scanner;

public class Main {
    private static final String VERTICAL_DELIMITER = "|";
    private static final String HORIZONTAL_DELIMITER = "-";
    private static final String CROSS_DELIMITER = "+";
    private static final String SPACE = " ";
    private static final String EOL = System.lineSeparator();

    public static void main(String[] args) {
        System.out.println("Enter the number of rows from 1 to 32:");
        var a = scanInt();
        var maxLen = String.valueOf(a * a).length();
        var maxLenFirstRow = String.valueOf(a).length();
        var n = 2 * (a + 1);
        for (var i = 0; i < n; i++) {
            for (var j = 0; j < a + 1; j++) {
                var value = getElementValue(i, j, maxLen, maxLenFirstRow);
                var delimiter = getDelimiter(i, j, a - 1);
                System.out.print(value + delimiter);
            }
        }
    }

    private static String getElementValue(int i, int j, int maxLen, int maxLenFirstRow) {
        var elemLen = j == 0 ? maxLenFirstRow : maxLen;
        if (i == 0 && j == 0) {
            return SPACE.repeat(elemLen);
        } else if (i % 2 == 0) {
            var i_val = i / 2;
            var value = (i == 0 || j == 0) ? i_val + j : i_val * j;
            return String.format("%" + elemLen + "d", value);
        } else {
            return HORIZONTAL_DELIMITER.repeat(elemLen);
        }
    }

    private static String getDelimiter(int i, int j, int n) {
        if (j == n + 1) {
            return EOL;
        }
        return i % 2 == 0 ? VERTICAL_DELIMITER : CROSS_DELIMITER;
    }

    private static int scanInt() {
        var sc = new Scanner(System.in);
        int value;
        while (true) {
            var input = sc.nextLine();
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