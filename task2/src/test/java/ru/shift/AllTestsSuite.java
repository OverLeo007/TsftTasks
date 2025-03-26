package ru.shift;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import ru.shift.utilsTests.ConstantsTest;
import ru.shift.utilsTests.ParsePositiveDoubleTest;
import ru.shift.utilsTests.ReadFromFileTest;


@Suite
@SelectClasses({
        FigureObjectsTest.class,
        FigureTypeEnumTest.class,
        ParsePositiveDoubleTest.class,
        ConstantsTest.class,
        ReadFromFileTest.class
})
public class AllTestsSuite {
    // Этот класс просто запускает другие тесты.
}