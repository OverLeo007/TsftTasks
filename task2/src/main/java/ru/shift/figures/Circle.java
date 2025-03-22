package ru.shift.figures;

public class Circle extends Figure {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public static Circle read(double[] params) {
        return new Circle(params[0]);
    }
}
