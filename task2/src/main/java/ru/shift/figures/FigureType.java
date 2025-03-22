package ru.shift.figures;

// FIXME: Перенести фабрику в фабирку
public enum FigureType {
  CIRCLE(1) {
    @Override
    public Figure createFigure(double[] params) {
      return new Circle(params[0]);
    }
  },
  RECTANGLE(2) {
    @Override
    public Figure createFigure(double[] params) {
      return Rectangle.create(params);
    }
  },
  TRIANGLE(3) {
    @Override
    public Figure createFigure(double[] params) {
      return Triangle.create(params);
    }
  };

  private final int expectedParams;

  FigureType(int expectedParams) {
    this.expectedParams = expectedParams;
  }

  public void validateParamCount(double[] params) {
    if (params.length != expectedParams) {
      throw new IllegalArgumentException(
          String.format("%s должен иметь %d параметров", this, expectedParams));
    }
  }

  public abstract Figure createFigure(double[] params);
}
