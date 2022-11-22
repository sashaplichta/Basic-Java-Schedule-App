package ui;

import model.*;

import java.io.FileNotFoundException;
import java.util.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the schedule app. JSON-related code used by permission of CPSC 210
public class ScheduleApp {
    private static final String JSON_STORE = "./data/schedule.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Schedule userSchedule = new Schedule();
    private Scanner input;
    private List<String> daysOfWeek = Arrays.asList(
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

    public ScheduleApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // Effects: takes user input and allows them to interface with app through the console
    // Attribution: structure copied from "TellerApp" code provided
    private void runApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // Modifies: input
    // Effects: initializes input for the app
    // Attribution: structure copied from "TellerApp" code provided
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // Requires: String command (really should be a character in all but one case, but for ease of use all are strings)
    // Effects: processes a user command and calls the appropriate handling function
    // Attribution: structure copied from "TellerApp" code provided
    private void processCommand(String command) {
        if (command.equals("a")) {
            addJob();
        } else if (command.equals("r")) {
            removeJob();
        } else if (command.equals("v")) {
            viewSchedule();
        } else if (command.equals("vj")) {
            viewJobs();
        } else if (command.equals("s")) {
            saveSchedule();
        } else if (command.equals("l")) {
            loadSchedule();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // Effects: prints out a list of a user's current jobs
    private void viewJobs() {
        List<Job> currentJobs = userSchedule.getJobList();
        System.out.println("\nYour current jobs are:");
        for (Job job : currentJobs) {
            System.out.println(job.getName());
        }
    }

    // Effects: prints out a menu of potential user commands
    // Attribution: structure copied from "TellerApp" code provided
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add a job");
        System.out.println("\tr -> remove a job");
        //System.out.println("\tc -> change a job");
        System.out.println("\tv -> view your schedule");
        System.out.println("\tvj -> view just your jobs");
        System.out.println("\ts -> save your schedule");
        System.out.println("\tl -> load saved schedule");
        System.out.println("\tq -> quit");
    }

    // Effects: Saves current schedule to JSON file
    // Note: Code appropriated with permission of CPSC 210
    private void saveSchedule() {
        try {
            jsonWriter.open();
            jsonWriter.write(userSchedule);
            jsonWriter.close();
            System.out.println("Saved your schedule to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads schedule from file
    // Note: code appropriated with permission of CPSC 210
    private void loadSchedule() {
        try {
            userSchedule = jsonReader.read();
            System.out.println("Loaded schedule from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // Modifies: this.userSchedule
    // Effects: reads user input necessary to create a Job (name and hours) and
    // adds a job of those specifications to userSchedule
    private void addJob() {
        String jobName;
        List<List<Integer>> hours = new ArrayList<>();
        System.out.println("\nEnter the name of the job to add:");
        jobName = input.next();

        System.out.println("\nEnter the hours to be worked in this format: 0 ENTER 1 ENTER... "
                + "followed by any number over 24 to move to the next day");
        for (String day : daysOfWeek) {
            System.out.println("\nEnter the hours for " + day);
            int in;
            List<Integer> dayHours = new ArrayList<>();
            in = input.nextInt();
            while (in < 25) {
                dayHours.add(in);
                in = input.nextInt();
            }
            hours.add(dayHours);
        }

        userSchedule.addJob(jobName, map(hours));

        System.out.println("\nJob Added!");
    }

    // Requires: a list of a list of hours (hours of each day of each week)
    // Effects: returns a dictionary mapping String days of the week ("Monday" etc) to hours that day
    private Map<String, List<Integer>> map(List<List<Integer>> hours) {
        Map<String, List<Integer>> dict = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            dict.put(daysOfWeek.get(i), hours.get(i));
        }

        return dict;
    }

    // Modifies: this.userSchedule
    // Effects: reads user input necessary to remove a Job (name) and
    // removes a job of this specification from userSchedule
    private void removeJob() {
        String jobName;
        System.out.println("\nEnter the name of the job to remove:");
        jobName = input.next();
        userSchedule.removeJob(jobName);
        System.out.println("\nJob Removed!");
    }

    // Effects: prints out a view of the user's schedule by job using a
    // (rows by columns) hours x days of the week format
    private void viewSchedule() {
        List<Job> currentJobs = userSchedule.getJobList();
        for (Job job : currentJobs) {
            System.out.println(job.getName());
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                    "Hour", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
            printJob(job);
        }
    }

    // Requires: job to be a Job
    // Effects: prints the hours for a job out in the format (rows by columns) hours x days of the week
    private void printJob(Job job) {
        List<Boolean> jobHours = new ArrayList<>();
        List<List<Boolean>> initialHours = job.getSchedJobHours();
        // List<Boolean> temp;

        for (int j = 0; j < initialHours.get(0).size(); j++) {
            // temp = new ArrayList<>();
            for (int i = 0; i < initialHours.size(); i++) {
                // temp.add(initialHours.get(j).get(i));
                jobHours.add(initialHours.get(i).get(j));
            }
            // jobHours.add(temp);
        }
        for (int i = 0; i < jobHours.size(); i += 7) {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", (i / 7),
                    jobHours.get(i), jobHours.get(i + 1), jobHours.get(i + 2), jobHours.get(i + 3),
                    jobHours.get(i + 4), jobHours.get(i + 5), jobHours.get(i + 6));
        }
    }
}
