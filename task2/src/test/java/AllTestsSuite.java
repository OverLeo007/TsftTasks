import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({FigureObjectsTest.class})
public class AllTestsSuite {
    // Этот класс просто запускает другие тесты.
}