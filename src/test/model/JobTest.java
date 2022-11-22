package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JobTest {
    private Job testJob;
    private List<Day> testDays;
    private Day blankDay;

    @BeforeEach
    public void RunBefore() {
        testJob = new Job("A");
        testDays = Arrays.asList(new Day("Sunday"),
                new Day("Monday"),
                new Day("Tuesday"),
                new Day("Wednesday"),
                new Day("Thursday"),
                new Day("Friday"),
                new Day("Saturday"));
        blankDay = new Day("blank");
    }

    @Test
    public void testConstructor() {
        assertEquals(testJob.getName(), "A");
        // assertEquals(testJob.getCompletedDays(), testDays);
        // assertEquals(testJob.getScheduledDays(), testDays);
        assertEquals(testJob.getCompletedDays().get(1).getName(), "Monday");
        assertEquals(testJob.getScheduledDays().get(1).getName(), "Monday");
    }

    @Test
    public void testGetHours() {
        assertEquals(testJob.getSchedJobHours(),
                Arrays.asList(blankDay.getHours(), blankDay.getHours(), blankDay.getHours(),
                        blankDay.getHours(), blankDay.getHours(), blankDay.getHours(), blankDay.getHours()));
        assertEquals(testJob.getCompJobHours(),
                Arrays.asList(blankDay.getHours(), blankDay.getHours(), blankDay.getHours(),
                        blankDay.getHours(), blankDay.getHours(), blankDay.getHours(), blankDay.getHours()));
    }

    @Test
    public void testAddHours() {
        testJob.addSchedHours(Arrays.asList("Monday", "Tuesday"), Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)));
        testJob.addCompHours(Arrays.asList("Monday", "Tuesday"), Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)));

        assertEquals(testJob.getSchedJobHours(),
                Arrays.asList(blankDay.getHours(),
                        Arrays.asList(false, true, true, true, false, false, false, false,
                                false, false, false, false, false, false, false, false, false, false, false, false,
                                false, false, false, false),
                        Arrays.asList(false, false, false, false, true, true, true, false,
                                false, false, false, false, false, false, false, false, false, false, false, false,
                                false, false, false, false),
                        blankDay.getHours(), blankDay.getHours(), blankDay.getHours(), blankDay.getHours()));
        assertEquals(testJob.getCompJobHours(),
                Arrays.asList(blankDay.getHours(),
                        Arrays.asList(false, true, true, true, false, false, false, false,
                                false, false, false, false, false, false, false, false, false, false, false, false,
                                false, false, false, false),
                        Arrays.asList(false, false, false, false, true, true, true, false,
                                false, false, false, false, false, false, false, false, false, false, false, false,
                                false, false, false, false),
                        blankDay.getHours(), blankDay.getHours(), blankDay.getHours(), blankDay.getHours()));
    }
}
