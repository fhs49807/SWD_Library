package at.ac.fhsalzburg.swd.spring.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class UnitTest {

    @Test
    void testAdd() {
        assertEquals(2, 1 + 1);
    }
}
