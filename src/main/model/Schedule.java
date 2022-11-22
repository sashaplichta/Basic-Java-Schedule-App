package model;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Class Schedule represents a user's schedule (effectively a list of Jobs, each with their own work schedules)
public class Schedule implements Writable {
    private List<Job> jobList;

    // Modifies: this.jobList
    // Effects: initializes jobList as an empty array
    public Schedule() {
        jobList = new ArrayList();
    }

    public List<Job> getJobList() {
        return this.jobList;
    }

    // Requires: String jobName, a dictionary (HashMap?) with
    // the keys being days of the week and the values being lists of hours to be worked that day
    // Modifies: this.jobList
    // Effects: Adds a job with the specified hours/days of week and given name to jobList
    public void addJob(String jobName, Map<String, List<Integer>> dailyHours) {
        List<String> changedDays = new ArrayList<>(dailyHours.keySet());
        List<List<Integer>> matchedHours = new ArrayList<>();
        for (String day : changedDays) {
            matchedHours.add(dailyHours.get(day));
        }
        Job newJob = new Job(jobName);
        newJob.addSchedHours(changedDays, matchedHours);
        jobList.add(newJob);
    }

    // Requires: String jobName to be within jobList
    // Modifies: this.jobList
    // Effects: removes a job with the given name (the first such job) from jobList
    // Note: in hindsight, I would have used sets for all of this,
    // but I'd already used lists for half so oh well, next time
    public void removeJob(String jobName) {
        List<String> jobNames = new ArrayList<>();
        for (Job job : jobList) {
            jobNames.add(job.getName());
        }
        jobList.remove(jobNames.indexOf(jobName));
    }

    // Requires: String jobname and Job job
    // Effects: Adds a job directly using a job rather than hours
    // Modifies: jobList
    public void addJobReboot(String name, Job job) {
        jobList.add(job);
    }

    // JSON code appropriated with permission of CPSC 210
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        // json.put("name", name);
        json.put("Jobs", jobsToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray jobsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Job j : jobList) {
            jsonArray.put(j.toJson());
        }

        return jsonArray;
    }
}
