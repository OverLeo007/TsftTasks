package ru.shift.utilsTests;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import ru.shift.cli.MyUtils;
import ru.shift.figures.FigureType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadFromFileTest {

    @Test
    void readParamsFromLine_ReturnParamsArrayWhenValidInput() throws IOException {
        // arrange
        FigureType figureType = mock(FigureType.class);
        when(figureType.getParamLineMaxLen()).thenReturn(10);
        BufferedReader reader = new BufferedReader(new StringReader("10 20 30\r\n"));

        // act
        String[] result = MyUtils.readParamsFromLine(reader, figureType);

        // assert
        assertThat(result).containsExactly("10", "20", "30");
        verify(figureType).validateParamCount(result);
    }

    @Test
    void readParamsFromLine_ThrowIllegalArgumentExceptionWhenEmptyString() {
        // arrange
        FigureType figureType = mock(FigureType.class);
        when(figureType.getParamLineMaxLen()).thenReturn(10);
        BufferedReader reader = new BufferedReader(new StringReader("\n"));

        // act & assert
        assertThatThrownBy(() -> MyUtils.readParamsFromLine(reader, figureType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Строка с параметрами фигуры не может быть пустой");
    }

    @Test
    void readParamsFromLine_ReadOnlyMaxAllowedCharacters() throws IOException {
        // arrange
        FigureType figureType = mock(FigureType.class);
        when(figureType.getParamLineMaxLen()).thenReturn(5);
        BufferedReader reader = new BufferedReader(new StringReader("123456789\n"));

        // act
        String[] result = MyUtils.readParamsFromLine(reader, figureType);

        // assert
        assertThat(result).containsExactly("12345"); // Только первые 5 символов
        verify(figureType).validateParamCount(result);
    }

    @Test
    void readParamsFromLine_IgnoreCarriageReturnAndStopAtNewline() throws IOException {
        // arrange
        FigureType figureType = mock(FigureType.class);
        when(figureType.getParamLineMaxLen()).thenReturn(10);
        BufferedReader reader = new BufferedReader(new StringReader("12\r\n34\n"));

        // act
        String[] result = MyUtils.readParamsFromLine(reader, figureType);

        // assert
        assertThat(result).containsExactly("12");

        // act
        result = MyUtils.readParamsFromLine(reader, figureType);

        // assert
        assertThat(result).containsExactly("34");
        verify(figureType).validateParamCount(result);
    }

    @Test
    void readParamsFromLine_StopReadingAtEOF() throws IOException {
        // arrange
        FigureType figureType = mock(FigureType.class);
        when(figureType.getParamLineMaxLen()).thenReturn(10);
        BufferedReader reader = new BufferedReader(new StringReader("100 200 30000000000"));

        // act
        String[] result = MyUtils.readParamsFromLine(reader, figureType);

        // assert
        assertThat(result).containsExactly("100", "200", "30");
        verify(figureType).validateParamCount(result);
    }

    @Test
    void readFigType_ReturnFigureTypeWhenValidInput() throws IOException {
        // arrange
        BufferedReader reader = new BufferedReader(new StringReader("CIRCLE\n"));

        // act
        String result = MyUtils.readFigType(reader);

        // assert
        assertThat(result).isEqualTo("CIRCLE");
    }

    @Test
    void readFigType_ThrowExceptionWhenFigureTypeIsEmpty() {
        // arrange
        BufferedReader reader = new BufferedReader(new StringReader("\n"));

        // act & assert
        assertThatThrownBy(() -> MyUtils.readFigType(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Название фигуры не может быть пустым");
    }

}
