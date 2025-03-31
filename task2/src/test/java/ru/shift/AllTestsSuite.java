package ru.shift;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import ru.shift.factory.test.FactoryTestsSuite;
import ru.shift.figure.tests.FigureTestsSuite;
import ru.shift.utils.tests.UtilTestsSuite;


@Suite
@SelectClasses({
        FigureTestsSuite.class,
        UtilTestsSuite.class,
        FactoryTestsSuite.class
})
public class AllTestsSuite {
}