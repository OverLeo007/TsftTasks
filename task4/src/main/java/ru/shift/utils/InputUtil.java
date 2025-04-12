package ru.shift.utils;

import java.util.Scanner;

public class InputUtil {
    public static long scanPositiveLong() {
        var sc = new Scanner(System.in);
        long number;
        while (true) {
            try {
                System.out.print("Enter a positive long number: ");
                number = sc.nextLong();
                if (number > 0) {
                    return number;
                } else {
                    System.out.println("The number must be positive.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a positive long number.");
                sc.next();
            }
        }
    }
}
