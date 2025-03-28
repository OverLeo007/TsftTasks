package ru.shift.factory.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        FactoryRegistryTest.class,
        TriangleFactoryTest.class,
        CircleFactoryTest.class,
        RectangleFactoryTest.class
})
public class FactoryTestsSuite {

}
