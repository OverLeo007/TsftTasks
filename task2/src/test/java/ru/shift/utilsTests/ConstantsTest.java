package ru.shift.utilsTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.shift.cli.MyUtils;

public class ConstantsTest {
    @Test
    void correctEofReturnCode() {
        assertThat(MyUtils.EOF_RETURN_CODE).isEqualTo(-1);
    }

    @Test
    void correctEndOfLine() {
        assertThat(MyUtils.EOL).isEqualTo(System.lineSeparator());
    }

}
