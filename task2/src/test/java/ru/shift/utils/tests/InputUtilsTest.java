package ru.shift.utils.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;
import ru.shift.utils.InputUtils;

public class InputUtilsTest {

    @Test
    void readParamsFromLine_ReturnParamsArrayWhenValidInput() throws IOException {
        // arrange
        var paramLineMaxLen = 10;
        BufferedReader reader = new BufferedReader(new StringReader("10 20 30\r\n"));

        // act
        String[] result = InputUtils.readParamsFromLine(reader, paramLineMaxLen);

        // assert
        assertThat(result).containsExactly("10", "20", "30");
    }

    @Test
    void readParamsFromLine_ThrowIllegalArgumentExceptionWhenEmptyString() {
        // arrange
        var paramLineMaxLen = 10;
        BufferedReader reader = new BufferedReader(new StringReader("\n"));

        // act & assert
        assertThatThrownBy(() -> InputUtils.readParamsFromLine(reader, paramLineMaxLen))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Строка с параметрами фигуры не может быть пустой");
    }

    @Test
    void readParamsFromLine_ReadOnlyMaxAllowedCharacters() throws IOException {
        // arrange
        var paramLineMaxLen = 5;
        BufferedReader reader = new BufferedReader(new StringReader("123456789\n"));

        // act
        String[] result = InputUtils.readParamsFromLine(reader, paramLineMaxLen);

        // assert
        assertThat(result).containsExactly("12345"); // Только первые 5 символов
    }

    @Test
    void readParamsFromLine_IgnoreCarriageReturnAndStopAtNewline() throws IOException {
        // arrange
        var paramLineMaxLen = 5;
        BufferedReader reader = new BufferedReader(new StringReader("12\r\n34\n"));

        // act
        String[] result = InputUtils.readParamsFromLine(reader, paramLineMaxLen);

        // assert
        assertThat(result).containsExactly("12");

        // act
        result = InputUtils.readParamsFromLine(reader, paramLineMaxLen);

        // assert
        assertThat(result).containsExactly("34");
    }

    @Test
    void readParamsFromLine_StopReadingAtEOF() throws IOException {
        // arrange
        var paramLineMaxLen = 10;
        BufferedReader reader = new BufferedReader(new StringReader("100 200 30000000000"));

        // act
        String[] result = InputUtils.readParamsFromLine(reader, paramLineMaxLen);

        // assert
        assertThat(result).containsExactly("100", "200", "30");
    }

    @Test
    void readFigType_ReturnFigureTypeWhenValidInput() throws IOException {
        // arrange
        BufferedReader reader = new BufferedReader(new StringReader("CIRCLE\n"));

        // act
        String result = InputUtils.readFigType(reader);

        // assert
        assertThat(result).isEqualTo("CIRCLE");
    }

    @Test
    void readFigType_ThrowExceptionWhenFigureTypeIsEmpty() {
        // arrange
        BufferedReader reader = new BufferedReader(new StringReader("\n"));

        // act & assert
        assertThatThrownBy(() -> InputUtils.readFigType(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Название фигуры не может быть пустым");
    }

}
