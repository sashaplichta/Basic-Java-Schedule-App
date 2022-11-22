package persistence;

import model.Schedule;
import model.Job;
import model.Day;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.json.*;
import java.util.*;

// Represents a reader that reads schedule from JSON data stored in file
public class JsonReader {
    private String source;
    private List<String> days =
            Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads schedule from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Schedule read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSchedule(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses schedule from JSON object and returns it
    private Schedule parseSchedule(JSONObject jsonObject) {
        // String name = jsonObject.getString("name");
        Schedule sched = new Schedule();
        addJobs(sched, jsonObject);
        return sched;
    }

    // MODIFIES: sched
    // EFFECTS: parses jobs from JSON object and adds them to schedule
    private void addJobs(Schedule sched, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Jobs");
        for (Object json : jsonArray) {
            JSONObject nextJob = (JSONObject) json;
            addJob(sched, nextJob);
        }
    }

    // MODIFIES: sched
    // EFFECTS: parses jobs from JSON object and adds it to sched
    @SuppressWarnings("methodlength")
    private void addJob(Schedule sched, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        List<List<Integer>> dayScheds = new ArrayList<>();
        List<List<Integer>> dayComps = new ArrayList<>();
        Job job = new Job(name);
        JSONArray schedDays = jsonObject.getJSONArray("scheduledDays");
        JSONArray compDays = jsonObject.getJSONArray("completedDays");

        for (int i = 0; i < schedDays.length(); i++) {
            JSONArray hours = schedDays.getJSONArray(i);
            List<Integer> tempList = new ArrayList<>();
            for (int j = 0; j < hours.length(); j++) {
                if (hours.getBoolean(j) == true) {
                    tempList.add(j);
                }
            }
            dayScheds.add(tempList);
        }

        for (int i = 0; i < compDays.length(); i++) {
            JSONArray hours = compDays.getJSONArray(i);
            List<Integer> tempList = new ArrayList<>();
            for (int j = 0; j < hours.length(); j++) {
                if (hours.getBoolean(j) == true) {
                    tempList.add(j);
                }
            }
            dayComps.add(tempList);
        }

        job.addSchedHours(days, dayScheds);
        job.addCompHours(days, dayComps);
        sched.addJobReboot(name, job);
    }
}
