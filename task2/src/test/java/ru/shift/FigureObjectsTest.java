package ru.shift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import ru.shift.figures.Circle;
import ru.shift.figures.FigureType;
import ru.shift.figures.Rectangle;
import ru.shift.figures.Triangle;

public class FigureObjectsTest {

    private static final String EOL = System.lineSeparator();
    private static final double STANDARD_OFFSET = 1e-3 * 5;

    private String[] splitParams(String params) {
        return params.split(" ");
    }

    /*------------------------------------------TRIANGLE------------------------------------------*/

    @Test
    void createTriangleCorrectParamsIntegers() {
        // arrange
        var params = splitParams("3 4 5");
        // act
        Triangle triangle = Triangle.createFromParamStrs(params);

        // assert
        assertThat(triangle.getType())
                .isEqualTo(FigureType.TRIANGLE);
        assertThat(triangle.computePerimeter())
                .isCloseTo(12.0, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeArea())
                .isCloseTo(6.0, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeBcAcAngle())
                .isCloseTo(36.87, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeAbAcAngle())
                .isCloseTo(53.13, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeAbBcAngle())
                .isCloseTo(90.0, byLessThan(STANDARD_OFFSET));
    }

    @Test
    void createTriangleCorrectParamsDoubles() {
        // arrange
        var params = splitParams("3.5 4.5 5.5");
        // act
        Triangle triangle = Triangle.createFromParamStrs(params);

        // assert
        assertThat(triangle.getType())
                .isEqualTo(FigureType.TRIANGLE);
        assertThat(triangle.computePerimeter())
                .isCloseTo(13.5, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeArea())
                .isCloseTo(7.854, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeBcAcAngle())
                .isCloseTo(39.4, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeAbAcAngle())
                .isCloseTo(54.695, byLessThan(STANDARD_OFFSET));
        assertThat(triangle.computeAbBcAngle())
                .isCloseTo(85.9, byLessThan(STANDARD_OFFSET));
    }


    @Test
    void createTriangle_throwsIllegalArgumentException_IfImpossibleTriangleParams_forASideBranch() {
        // arrange
        var params = splitParams("3 1 2");
        // act
        assertThatThrownBy(() -> Triangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Треугольник со сторонами 3,00 1,00 2,00 невозможен");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfImpossibleTriangleParams_forBSideBranch() {
        // arrange
        var params = splitParams("1 3 2");
        // act
        assertThatThrownBy(() -> Triangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Треугольник со сторонами 1,00 3,00 2,00 невозможен");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfImpossibleTriangleParams_forCSideBranch() {
        // arrange
        var params = splitParams("1 2 3");
        // act
        assertThatThrownBy(() -> Triangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Треугольник со сторонами 1,00 2,00 3,00 невозможен");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfNegativeValueParams() {
        // arrange
        var params = splitParams("1 2 -3");
        // act
        assertThatThrownBy(() -> Triangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "При попытке преобразования аргументов в число произошла ошибка");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfNotNumericValueParams() {
        // arrange
        var params = splitParams("1 2 b");
        // act
        assertThatThrownBy(() -> Triangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "При попытке преобразования аргументов в число произошла ошибка");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfBlankParamsLine() {
        // arrange
        var params = splitParams("");
        // act
        assertThatThrownBy(() -> Triangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Строка не может быть пустой");
    }

    @Test
    void writeTriangleDataToWriter() throws IOException {
        // arrange
        BufferedWriter writer = mock(BufferedWriter.class);
        Triangle triangle = Triangle.createFromParamStrs(splitParams("3 4 5"));

        // act
        String expected = (
                """
                        Тип фигуры: TRIANGLE
                        Площадь: 6 см²
                        Периметр: 12 см
                        Сторона - Длина - Противолежащий угол:
                        AB - 3 см - 36,87°
                        BC - 4 см - 53,13°
                        AC - 5 см - 90°
                        """
        ).replace("\n", EOL);
        triangle.writeFigureData(writer);

        // assert
        verify(writer, times(1)).write(expected);
    }

    /*------------------------------------------RECTANGLE-----------------------------------------*/


    @Test
    void createRectangleCorrectParamsIntegers() {
        // arrange
        var params = splitParams("3 4");
        // act
        Rectangle rectangle = Rectangle.createFromParamStrs(params);

        // assert
        assertThat(rectangle.getType())
                .isEqualTo(FigureType.RECTANGLE);
        assertThat(rectangle.computePerimeter())
                .isCloseTo(14.0, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.computeArea())
                .isCloseTo(12.0, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.computeDiagonal())
                .isCloseTo(5, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.getLength())
                .isCloseTo(4, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.getWidth())
                .isCloseTo(3, byLessThan(STANDARD_OFFSET));
    }

    @Test
    void createRectangleCorrectParamsDoubles() {
        // arrange
        var params = splitParams("3.5 4.5");
        // act
        Rectangle rectangle = Rectangle.createFromParamStrs(params);

        // assert
        assertThat(rectangle.getType())
                .isEqualTo(FigureType.RECTANGLE);
        assertThat(rectangle.computePerimeter())
                .isCloseTo(16.0, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.computeArea())
                .isCloseTo(15.75, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.computeDiagonal())
                .isCloseTo(5.7, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.getLength())
                .isCloseTo(4.5, byLessThan(STANDARD_OFFSET));
        assertThat(rectangle.getWidth())
                .isCloseTo(3.5, byLessThan(STANDARD_OFFSET));
    }


    @Test
    void createRectangle_throwsIllegalArgumentException_IfNegativeValues() {
        // arrange
        var params = splitParams("1 -2");
        // act
        assertThatThrownBy(() -> Rectangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "При попытке преобразования аргументов в число произошла ошибка");
    }

    @Test
    void createRectangle_throwsIllegalArgumentException_IfNotNumericValues() {
        // arrange
        var params = splitParams("1 b");
        // act
        assertThatThrownBy(() -> Rectangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "При попытке преобразования аргументов в число произошла ошибка");
    }

    @Test
    void createRectangle_throwsIllegalArgumentException_IfBlankParamsLine() {
        // arrange
        var params = splitParams("");
        // act
        assertThatThrownBy(() -> Rectangle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Строка не может быть пустой");
    }

    @Test
    void writeRectangleDataToWriter() throws IOException {
        // arrange
        BufferedWriter writer = mock(BufferedWriter.class);
        Rectangle rectangle = Rectangle.createFromParamStrs(splitParams("3 5"));

        // act
        String expected = (
                """
                        Тип фигуры: RECTANGLE
                        Площадь: 15 см²
                        Периметр: 16 см
                        Диагональ: 5,83 см
                        Длина: 5 см
                        Ширина: 3 см
                        """
        ).replace("\n", EOL);
        rectangle.writeFigureData(writer);

        // assert
        verify(writer, times(1)).write(expected);
    }

    /*-------------------------------------------CIRCLE-------------------------------------------*/
    @Test
    void createCircleCorrectParamsIntegers() {
        // arrange
        var params = splitParams("3");
        // act
        Circle circle = Circle.createFromParamStrs(params);

        // assert
        assertThat(circle.getType())
                .isEqualTo(FigureType.CIRCLE);
        assertThat(circle.computePerimeter())
                .isCloseTo(18.849, byLessThan(STANDARD_OFFSET));
        assertThat(circle.computeArea())
                .isCloseTo(28.274, byLessThan(STANDARD_OFFSET));
        assertThat(circle.getRadius())
                .isCloseTo(3, byLessThan(STANDARD_OFFSET));
        assertThat(circle.computeDiameter())
                .isCloseTo(6, byLessThan(STANDARD_OFFSET));
    }

    @Test
    void createCircleCorrectParamsDoubles() {
        // arrange
        var params = splitParams("3.5");
        // act
        Circle circle = Circle.createFromParamStrs(params);

        // assert
        assertThat(circle.getType())
                .isEqualTo(FigureType.CIRCLE);
        assertThat(circle.computePerimeter())
                .isCloseTo(21.991, byLessThan(STANDARD_OFFSET));
        assertThat(circle.computeArea())
                .isCloseTo(38.484, byLessThan(STANDARD_OFFSET));
        assertThat(circle.getRadius())
                .isCloseTo(3.5, byLessThan(STANDARD_OFFSET));
        assertThat(circle.computeDiameter())
                .isCloseTo(7, byLessThan(STANDARD_OFFSET));
    }

    @Test
    void createCircle_throwsIllegalArgumentException_IfNegativeValue() {
        // arrange
        var params = splitParams("-3");
        // act
        assertThatThrownBy(() -> Circle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "При попытке преобразования аргументов в число произошла ошибка");
    }

    @Test
    void createCircle_throwsIllegalArgumentException_IfNotNumericValue() {
        // arrange
        var params = splitParams("b");
        // act
        assertThatThrownBy(() -> Circle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "При попытке преобразования аргументов в число произошла ошибка");
    }

    @Test
    void createCircle_throwsIllegalArgumentException_IfBlankParamsLine() {
        // arrange
        var params = splitParams("");
        // act
        assertThatThrownBy(() -> Circle.createFromParamStrs(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Строка не может быть пустой");
    }

    @Test
    void writeCircleDataToWriter() throws IOException {
        // arrange
        BufferedWriter writer = mock(BufferedWriter.class);
        Circle circle = Circle.createFromParamStrs(splitParams("10"));

        // act
        String expected = (
                """
                        Тип фигуры: CIRCLE
                        Площадь: 314,16 см²
                        Периметр: 62,83 см
                        Радиус: 10 см
                        Диаметр: 20 см
                        """
        ).replace("\n", EOL);
        circle.writeFigureData(writer);

        // assert
        verify(writer, times(1)).write(expected);
    }
}
