package ru.shift.utils.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import ru.shift.utils.ParseUtils;

public class ParseUtilsTest {

    @Test
    void parsePositiveDouble_ValidInput() {
        // arrange
        String input1 = "1.0";
        String input2 = "10.5";
        String input3 = "100";

        // act & assert
        assertThat(ParseUtils.parsePositiveDouble(input1)).isEqualTo(1.0);
        assertThat(ParseUtils.parsePositiveDouble(input2)).isEqualTo(10.5);
        assertThat(ParseUtils.parsePositiveDouble(input3)).isEqualTo(100.0);
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void parsePositiveDouble_ThrowsForNull() {
        // arrange
        String input = null;

        // act & assert
        assertThatThrownBy(() -> ParseUtils.parsePositiveDouble(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Строка не может быть пустой");
    }



    @Test
    void parsePositiveDouble_ThrowsForEmptyString() {
        // arrange
        String input = "   ";

        // act & assert
        assertThatThrownBy(() -> ParseUtils.parsePositiveDouble(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Строка не может быть пустой");
    }

    @Test
    void parsePositiveDouble_ThrowsForInvalidNumber() {
        // arrange
        String input = "abc";

        // act & assert
        assertThatThrownBy(() -> ParseUtils.parsePositiveDouble(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Значение должно быть числом, но получено: \"abc\"");
    }

    @Test
    void parsePositiveDouble_ThrowsForNegativeNumber() {
        // arrange
        String input = "-5";

        // act & assert
        assertThatThrownBy(() -> ParseUtils.parsePositiveDouble(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Значение должно быть >= 1");
    }

    @Test
    void parsePositiveDouble_ThrowsForZero() {
        // arrange
        String input = "0";

        // act & assert
        assertThatThrownBy(() -> ParseUtils.parsePositiveDouble(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Значение должно быть >= 1");
    }

    @Test
    void parsePositiveDouble_ThrowsForNumberLessThanOne() {
        // arrange
        String input = "0.99";

        // act & assert
        assertThatThrownBy(() -> ParseUtils.parsePositiveDouble(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Значение должно быть >= 1");
    }




}
