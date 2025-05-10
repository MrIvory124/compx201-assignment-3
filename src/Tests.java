import org.junit.jupiter.api.*;

public class Tests {

    @Test
    @DisplayName("Test Hello World Method")
    void helloWorldTest() {
        int actual = 1;
        Assertions.assertEquals(1, actual);
    }
}
