package ru.shift;

import java.util.Scanner;

public class IoManager {
    public static int scanInt() {
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

    public static void printLines(String ...lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        System.out.print(sb);
    }
}
