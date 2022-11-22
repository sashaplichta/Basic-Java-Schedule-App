package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DayTest {
    private Day testDay;
    private List<Boolean> testZeroHours;
    private List<Integer> testHours;
    private List<Boolean> hourTestAnswer;

    @BeforeEach
    public void RunBefore() {
        testDay = new Day("Monday");
        testZeroHours = Arrays.asList(false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false);
        testHours = Arrays.asList(1, 2, 3, 6, 7);
        hourTestAnswer = Arrays.asList(false, true, true, true, false, false, true, true,
                false, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false);

    }

    @Test
    public void testConstructor() {
        assertEquals(testDay.getName(), "Monday");
        assertEquals(testDay.getHours(), testZeroHours);
    }

    @Test
    public void testAddHours() {
        testDay.addHours(testHours);
        assertEquals(testDay.getHours(), hourTestAnswer);
    }

}
