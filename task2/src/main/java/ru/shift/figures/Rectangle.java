package ru.shift.figures;

public class Rectangle extends Figure {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public static Rectangle create(double[] params) {
        return new Rectangle(params[0], params[1]);
    }
}
