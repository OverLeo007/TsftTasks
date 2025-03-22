package ru.shift.figures;

public class Triangle extends Figure {
    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static Triangle create(double[] params) {
        return new Triangle(params[0], params[1], params[2]);
    }
}
