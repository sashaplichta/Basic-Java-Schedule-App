package model;

import org.json.JSONObject;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Class Job represents a job object with fields representing the job name,
// the job's work schedule (list of Day), and the completed hours (list of Day)
public class Job implements Writable {
    private String name;
    private List<Day> scheduledDays = new ArrayList<>();
    private List<Day> completedDays = new ArrayList<>();
    private List<String> stringsOfWeek;

    // Requires: String job name
    // Modifies: stringsOfWeek, this.name, this.scheduledDays, this.completedDays
    // Effects: sets this.name to the provided job name,
    // initializes stringsOfWeek to the days of the week,
    // initializes both scheduledDays and completedDays with each day of the week
    public Job(String name) {
        stringsOfWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        this.name = name;
        for (String day : stringsOfWeek) {
            scheduledDays.add(new Day(day));
            completedDays.add(new Day(day));
        }
    }

    public String getName() {
        return this.name;
    }

    public List<Day> getScheduledDays() {
        return scheduledDays;
    }

    public List<Day> getCompletedDays() {
        return completedDays;
    }

    // Effects: returns a list of lists of the hours of each day of the week
    public List<List<Boolean>> getSchedJobHours() {
        List<List<Boolean>> totalSched = new ArrayList<>();
        for (Day day : scheduledDays) {
            totalSched.add(day.getHours());
        }
        return totalSched;
    }

    // Effects: returns a list of lists of the hours of each day of the week
    public List<List<Boolean>> getCompJobHours() {
        List<List<Boolean>> totalComp = new ArrayList<>();
        for (Day day : completedDays) {
            totalComp.add(day.getHours());
        }
        return totalComp;
    }

    // Requires: a list of days (non-duplicate within "Monday", "Tuesday", etc
    // and a list of a list on integers representing hours to be worked on that day
    // Modifies: this.scheduledDays
    // Effects: updates this.scheduledDays with the days and hours specified
    public void addSchedHours(List<String> dayNames, List<List<Integer>> hours) {
        int numChange = dayNames.size();
        for (int i = 0; i < numChange; i++) {
            int dayIndex = stringsOfWeek.indexOf(dayNames.get(i));
            Day dayToChange = scheduledDays.get(dayIndex);
            dayToChange.addHours(hours.get(i));
            scheduledDays.set(dayIndex, dayToChange);
        }
    }

    // Requires: a list of days (non-duplicate within "Monday", "Tuesday", etc
    // and a list of a list on integers representing hours worked on that day
    // Modifies: this.completedDays
    // Effects: updates this.completedDays with the days and hours specified
    public void addCompHours(List<String> dayNames, List<List<Integer>> hours) {
        int numChange = dayNames.size();
        for (int i = 0; i < numChange; i++) {
            int dayIndex = stringsOfWeek.indexOf(dayNames.get(i));
            Day dayToChange = completedDays.get(dayIndex);
            dayToChange.addHours(hours.get(i));
            completedDays.set(dayIndex, dayToChange);
        }
    }

    // From 210 provided code
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsched = new JSONArray();
        JSONArray jcomp = new JSONArray();

        for (Day day : scheduledDays) {
            JSONArray hours = new JSONArray();
            for (int i = 0; i < day.getHours().size(); i++) {
                hours.put(day.getHours().get(i));
            }
            jsched.put(hours);
        }
        for (Day day : completedDays) {
            JSONArray hours = new JSONArray();
            for (int i = 0; i < day.getHours().size(); i++) {
                hours.put(day.getHours().get(i));
            }
            jcomp.put(hours);
        }

        json.put("name", name);
        json.put("scheduledDays", jsched);
        json.put("completedDays", jcomp);
        return json;
    }
}
