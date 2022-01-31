import annotation.After;
import annotation.Before;
import annotation.Test;

public class SimpleTest {
    @Before
    void setUp() {
        System.out.println("@Before: " + this);
    }

    @After
    void tearDown() {
        System.out.println("@After: " + this);
    }

    @Test
    void successTest1() {
        System.out.println("successTest1: " + this);
    }

    @Test
    void successTest2() {
        System.out.println("successTest2: " + this);
    }

    @Test
    void failedTest1() {
        System.out.println("failedTest1: " + this);
        throw new RuntimeException("Unexpected Error in failedTest1");
    }
}
