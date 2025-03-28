package ru.shift.figure.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        TriangleTest.class,
        CircleTest.class,
        RectangleTest.class
})
public class FigureTestsSuite {

    static final String EOL = System.lineSeparator();
    static final double STANDARD_OFFSET = 1e-3 * 5;

}
