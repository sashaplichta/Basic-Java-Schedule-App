package persistence;

import model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class JsonWriterTest {
    private Map<String, List<Integer>> jobHours;
    private Schedule testSchedule = new Schedule();
    private static final String JSON_STORE = "./data/schedule.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter failJsonWriter;
    private Schedule product = new Schedule();

    @BeforeEach
    public void runBefore() {
        jsonWriter = new JsonWriter(JSON_STORE);
        failJsonWriter = new JsonWriter("./data/DNE.json");
        jsonReader = new JsonReader(JSON_STORE);

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

        jobHours = new HashMap<String, List<Integer>>() {{
            put("Monday", mondayHours);
            put("Tuesday", tuesdayHours);
        }};

        testSchedule.addJob("A", jobHours);
    }

    @Test
    public void writeTest() {
        try {
            jsonWriter.open();
            jsonWriter.write(testSchedule);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }

        try {
            product = jsonReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }

        assertEquals(product.getJobList().get(0).getName(), testSchedule.getJobList().get(0).getName());

        try {
            failJsonWriter.open();
            failJsonWriter.write(testSchedule);
            failJsonWriter.close();
            fail("Shouldn't be a file");
        } catch (FileNotFoundException e) {
            // pass
        }
    }
}
