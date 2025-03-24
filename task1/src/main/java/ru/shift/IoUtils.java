package ru.shift;

import java.util.Scanner;

public class IoUtils {

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

    public static void print(String line) {
        System.out.print(line);
    }
}
