package ru.shift.factory.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;
import ru.shift.factory.FactoriesRegistry;
import ru.shift.factory.FigureType;

public class FactoryRegistryTest {

    @Test
    void getCircleFactory() throws IOException {
        // arrange
        var reader = new BufferedReader(new StringReader("CIRCLE"));
        // act

        var factory = FactoriesRegistry.getFactory(reader);

        // assert
        assertThat(factory.getType()).isEqualTo(FigureType.CIRCLE);
    }

    @Test
    void getRectangleFactory() throws IOException {
        // arrange
        var reader = new BufferedReader(new StringReader("RECTANGLE"));
        // act

        var factory = FactoriesRegistry.getFactory(reader);

        // assert
        assertThat(factory.getType()).isEqualTo(FigureType.RECTANGLE);
    }

    @Test
    void getTriangleFactory() throws IOException {
        // arrange
        var reader = new BufferedReader(new StringReader("TRIANGLE"));
        // act

        var factory = FactoriesRegistry.getFactory(reader);

        // assert
        assertThat(factory.getType()).isEqualTo(FigureType.TRIANGLE);
    }

    @Test
    void getUnknownTypeFactory_ThrowsIllegalArgumentException() {
        // arrange
        var figureName = "UNKNOWN";
        var reader = new BufferedReader(new StringReader(figureName));
        // act & assert
        assertThatThrownBy(() -> FactoriesRegistry.getFactory(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Неизвестный тип фигуры: " + figureName);
    }



}
