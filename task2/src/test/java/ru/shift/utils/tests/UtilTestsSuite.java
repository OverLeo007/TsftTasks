package ru.shift.utils.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ParseUtilsTest.class,
        InputUtilsTest.class
})
public class UtilTestsSuite {

}
