package model;

import java.util.List;
import java.util.ArrayList;

// Class Day represents a day object with a name of the week and 24 boolean hours that can
// either be scheduled (or completed depending on the content) when they are true and are
// otherwise free (false)
public class Day {
    private static Integer hoursInDay = 24;
    private String name;
    private List<Boolean> hours = new ArrayList<>();

    // Requires name within: Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    // Modifies: this.name, this.hours
    // Effects: Initializes this.name as the provided day of the week,
    // and all values within the hours list to False, ie "no work"
    public Day(String name) {
        for (int i = 0; i < hoursInDay; i++) {
            hours.add(false);
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<Boolean> getHours() {
        return this.hours;
    }

    // Requires: workHours to be a list of non-repeating integers between 0 and 23 inclusive
    // Modifies: this.hours
    // Effects: Sets the hours within workHours to true (busy) for the day
    public void addHours(List<Integer> workHours) {
        for (int hour : workHours) {
            this.hours.set(hour, true);
        }
    }
}
