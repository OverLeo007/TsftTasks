package ru.shift.figure.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import ru.shift.figures.Triangle;

/**
 * EOL and STANDARD_OFFSET are defined in {@link FigureTestsSuite}
 */
public class TriangleTest {
    @Test
    void createTriangleCorrectParamsIntegers() {
        // arrange & act
        Triangle triangle = new Triangle(3, 4, 5);

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
        // arrange & act
        Triangle triangle = new Triangle(3.5, 4.5, 5.5);

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
        // arrange & act
        assertThatThrownBy(() -> new Triangle(3, 1, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Треугольник со сторонами 3,00 1,00 2,00 невозможен");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfImpossibleTriangleParams_forBSideBranch() {
        // arrange
        // arrange & act
        assertThatThrownBy(() -> new Triangle(1, 3, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Треугольник со сторонами 1,00 3,00 2,00 невозможен");
    }

    @Test
    void createTriangle_throwsIllegalArgumentException_IfImpossibleTriangleParams_forCSideBranch() {
        // arrange & act & assert
        assertThatThrownBy(() -> new Triangle(1, 2, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Треугольник со сторонами 1,00 2,00 3,00 невозможен");
    }

    @Test
    void writeTriangleDataToWriter() throws IOException {
        // arrange
        BufferedWriter writer = mock(BufferedWriter.class);
        Triangle triangle = new Triangle(3, 4, 5);

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

}
