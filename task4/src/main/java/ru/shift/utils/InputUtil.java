package ru.shift.utils;

import java.util.Scanner;

public final class InputUtil {

    private InputUtil() {
        // Prevent instantiation
    }

    public static long scanPositiveLong() {
        var sc = new Scanner(System.in);
        long number;
        while (true) {
            try {
                number = sc.nextLong();
                if (number > 0) {
                    return number;
                } else {
                    System.out.println("The number must be positive.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a positive long type number.");
                sc.next();
            }
        }
    }

    public static int scanPositiveInt() {
        var sc = new Scanner(System.in);
        int number;
        while (true) {
            try {
                number = sc.nextInt();
                if (number > 0) {
                    return number;
                } else {
                    System.out.println("The number must be positive.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                sc.next();
            }
        }
    }
}
