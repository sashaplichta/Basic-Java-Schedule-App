package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {
    private Schedule testSchedule;
    private Schedule testScheduleAddedA;
    private Schedule testScheduleAddedAB;
    private Schedule testScheduleRemovedA;
    private Schedule testScheduleRemovedB;
    private Map<String, List<Integer>> jobAHours;
    private Map<String, List<Integer>> jobBHours;
    private Job jobA;
    private Job jobB;

    @BeforeEach
    void runBefore() {
        List<Integer> mondayHours = new ArrayList<Integer>() {{
            add(1);
            add(12);
            add(13);
        }};

        List<Integer> tuesdayHours = new ArrayList<Integer>() {{
            add(3);
            add(14);
            add(15);
        }};

        jobAHours = new HashMap<String, List<Integer>>() {{
            put("Monday", mondayHours);
            put("Tuesday", tuesdayHours);
        }};

        jobBHours = new HashMap<String, List<Integer>>() {{
            put("Wednesday", mondayHours);
            put("Friday", tuesdayHours);
        }};

        jobA = new Job("A");
        List<String> jobADays = new ArrayList<String>();
        for(String key : jobAHours.keySet()){
            jobADays.add(key);
        }
        List<List<Integer>> jobADayHours = new ArrayList<List<Integer>>();

        for (String day : jobADays) {
            jobADayHours.add(jobAHours.get(day));
        }

        jobB = new Job("B");
        List<String> jobBDays = new ArrayList<String>();
        for(String key : jobBHours.keySet()){
            jobBDays.add(key);
        }
        List<List<Integer>> jobBDayHours = new ArrayList<List<Integer>>();

        for (String day : jobBDays) {
            jobBDayHours.add(jobBHours.get(day));
        }

        jobA.addSchedHours(jobADays, jobADayHours);
        jobB.addSchedHours(jobBDays, jobBDayHours);

        testSchedule = new Schedule();
        testScheduleAddedA = new Schedule();
        testScheduleAddedAB = new Schedule();
        testScheduleRemovedA = new Schedule();
        testScheduleRemovedB = new Schedule();

        testScheduleAddedA.addJob("A", jobAHours);
        testScheduleAddedAB.addJob("A", jobAHours);
        testScheduleAddedAB.addJob("B", jobBHours);
        testScheduleRemovedA.addJob("A", jobAHours);
        testScheduleRemovedA.addJob("B", jobBHours);
        testScheduleRemovedB.addJob("A", jobAHours);
        testScheduleRemovedB.addJob("B", jobBHours);
        testScheduleRemovedA.removeJob("A");
        testScheduleRemovedB.removeJob("A");
        testScheduleRemovedB.removeJob("B");
    }

    @Test
    void testConstructor() {
        assertTrue(testSchedule.getJobList().isEmpty());
    }

    @Test
    void testAddJob() {
        assertEquals(testScheduleAddedA.getJobList().get(0).getName(), "A");
        assertEquals(testScheduleAddedAB.getJobList().get(1).getName(), "B");
    }

    @Test
    void testRemoveJob() {
        assertEquals(testScheduleRemovedA.getJobList().get(0).getName(), "B");
        assertTrue(testScheduleRemovedB.getJobList().isEmpty());
    }
}