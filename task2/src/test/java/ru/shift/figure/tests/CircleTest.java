package ru.shift.figure.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.shift.figure.tests.FigureTestsSuite.EOL;
import static ru.shift.figure.tests.FigureTestsSuite.STANDARD_OFFSET;

import java.io.BufferedWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import ru.shift.factory.FigureType;
import ru.shift.figures.Circle;

/**
 * EOL and STANDARD_OFFSET are defined in {@link FigureTestsSuite}
 */
public class CircleTest {

    @Test
    void createCircleCorrectParamsIntegers() {
        // arrange
        // act
        Circle circle = new Circle(3);

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
        // arrange & act
        Circle circle = new Circle(3.5);

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
    void writeCircleDataToWriter() throws IOException {
        // arrange
        BufferedWriter writer = mock(BufferedWriter.class);
        Circle circle = new Circle(10);

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
