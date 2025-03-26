package ru.shift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import ru.shift.figures.FigureType;

public class FigureTypeEnumTest {

    @Test
    void circleParamsCount_MaxParamLineLen_switchCaseParamLineGetter() {
        // arrange
        var paramsCount = FigureType.CIRCLE.getExpectedParamsCount();
        var expectedMaxLineLen = paramsCount * String.valueOf(Double.MAX_VALUE).length();

        // assert
        assertThat(paramsCount).isEqualTo(1);
        assertThat(FigureType.CIRCLE_PARAM_MAX_LINE_LEN)
                .isEqualTo(expectedMaxLineLen);
        assertThat(FigureType.CIRCLE_PARAM_MAX_LINE_LEN).isEqualTo(FigureType.CIRCLE.getParamLineMaxLen());
    }

    @Test
    void rectangleParamsCount_MaxParamLineLen_switchCaseParamLineGetter() {
        // arrange
        var paramsCount = FigureType.RECTANGLE.getExpectedParamsCount();
        var expectedMaxLineLen = paramsCount * String.valueOf(Double.MAX_VALUE).length();

        // assert
        assertThat(paramsCount).isEqualTo(2);
        assertThat(FigureType.RECTANGLE_PARAM_LINE_LEN)
                .isEqualTo(expectedMaxLineLen);
        assertThat(FigureType.RECTANGLE_PARAM_LINE_LEN).isEqualTo(FigureType.RECTANGLE.getParamLineMaxLen());
    }

    @Test
    void triangleParamsCount_MaxParamLineLen_switchCaseParamLineGetter() {
        // arrange
        var paramsCount = FigureType.TRIANGLE.getExpectedParamsCount();
        var expectedMaxLineLen = paramsCount * String.valueOf(Double.MAX_VALUE).length();

        // assert
        assertThat(paramsCount).isEqualTo(3);
        assertThat(FigureType.TRIANGLE_PARAM_LINE_LEN)
                .isEqualTo(expectedMaxLineLen);
        assertThat(FigureType.TRIANGLE_PARAM_LINE_LEN).isEqualTo(FigureType.TRIANGLE.getParamLineMaxLen());
    }


    @Test
    void getFigureTypeFromStringSwitchCase() {
        // arrange
        var unknownFigureType = "UNKNOWN";

        // assert
        assertThat(FigureType.fromString("CIRCLE")).isEqualTo(FigureType.CIRCLE);
        assertThat(FigureType.fromString("RECTANGLE")).isEqualTo(FigureType.RECTANGLE);
        assertThat(FigureType.fromString("TRIANGLE")).isEqualTo(FigureType.TRIANGLE);
        assertThatThrownBy(() -> FigureType.fromString(unknownFigureType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Неизвестный тип фигуры: " + unknownFigureType);
    }

    @Test
    void validationParamsCountForEveryFigureType_throwsIfIncorrectParamsCount() {
        // arrange
        var circle = FigureType.CIRCLE;
        var rectangle = FigureType.RECTANGLE;
        var triangle = FigureType.TRIANGLE;

        // assert
        assertThatThrownBy(() -> circle.validateParamCount(new Object[] {1.0, 2.0}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CIRCLE должен иметь 1 параметров");
        assertThatThrownBy(() -> rectangle.validateParamCount(new Object[] {1.0}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RECTANGLE должен иметь 2 параметров");
        assertThatThrownBy(() -> triangle.validateParamCount(new Object[] {1.0, 2.0, 3.0, 4.0}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TRIANGLE должен иметь 3 параметров");

    }


}
