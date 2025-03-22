package ru.shift.figures;


public class FigureFactory {
    public static Figure createFigure(FigureType figureType, double[] params) {
        figureType.validateParamCount(params);
        return figureType.createFigure(params);
    }
}
