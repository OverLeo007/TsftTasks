package ru.shift.factory.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shift.factory.FactoriesRegistry;
import ru.shift.factory.FigureType;
import ru.shift.factory.RectangleFactory;

public class RectangleFactoryTest {

    private RectangleFactory factory;

    @BeforeEach
    void setUp() throws IOException {
        factory = (RectangleFactory) FactoriesRegistry.getFactory(
                new BufferedReader(new StringReader("RECTANGLE"))
        );
    }

    @Test
    void getType() {
        // act & assert
        assertThat(factory.getType()).isEqualTo(FigureType.RECTANGLE);
    }

    @Test
    void getParamsCount() {
        // act & assert
        assertThat(factory.getParamsCount()).isEqualTo(2);
    }

    @Test
    void createFigure_CorrectParams() throws IOException {
        // arrange
        var reader = new BufferedReader(new StringReader("1 2"));
        // act & assert
        assertThat(factory.createFigure(reader))
                .hasFieldOrPropertyWithValue("width", 1.0)
                .hasFieldOrPropertyWithValue("height", 2.0);
    }

    @Test
    void createFigure_IncorrectParamsAmount_ThrowsIllegalArgumentException() {
        // arrange
        var reader = new BufferedReader(new StringReader("1"));
        // act & assert
        assertThatThrownBy(() -> factory.createFigure(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RECTANGLE должен иметь 2 параметров");
    }

    @Test
    void createFigure_LettersInParams_ThrowsIllegalArgumentException() {
        // arrange
        var reader = new BufferedReader(new StringReader("1 abc"));
        // act & assert
        assertThatThrownBy(() -> factory.createFigure(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Значение должно быть числом, но получено: \"abc\"");
    }

    @Test
    void createFigure_NegativeParams_ThrowsIllegalArgumentException() {
        // arrange
        var reader = new BufferedReader(new StringReader("-1 2"));
        // act & assert
        assertThatThrownBy(() -> factory.createFigure(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Значение должно быть >= 1");
    }

    @Test
    void createFigure_BlankLine_ThrowsIllegalArgumentException() {
        // arrange
        var reader = new BufferedReader(new StringReader(" "));
        // act & assert
        assertThatThrownBy(() -> factory.createFigure(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Строка с параметрами фигуры не может быть пустой");
    }

}
