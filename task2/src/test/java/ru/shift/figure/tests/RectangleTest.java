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
import ru.shift.figures.Rectangle;

/**
 * EOL and STANDARD_OFFSET are defined in {@link FigureTestsSuite}
 */
public class RectangleTest {

    @Test
    void createRectangleCorrectParamsIntegers() {
        // arrange & act
        Rectangle rectangle = new Rectangle(3, 4);

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
        // arrange & act
        Rectangle rectangle = new Rectangle(3.5, 4.5);

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
    void writeRectangleDataToWriter() throws IOException {
        // arrange
        BufferedWriter writer = mock(BufferedWriter.class);
        Rectangle rectangle = new Rectangle(3, 5);

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

}
