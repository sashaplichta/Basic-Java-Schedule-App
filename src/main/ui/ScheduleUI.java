package ui;

import ui.ScheduleApp;
import model.*;
import persistence.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import persistence.JsonReader;
import persistence.JsonWriter;

// Runs the UI for the Schedule App
public class ScheduleUI {
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;
    private JFrame frame;

    private JPanel contentPane;
    private JPanel editHeader;
    private JTable editSummary;
    private JTable summaryTable;
    private JPanel editScroll;
    private JTable scrollTable;
    private JLabel headerLabel;

    private JButton newButton;
    private JButton loadButton;
    private Schedule userSchedule;
    private Boolean load = false;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    java.util.List<String> jobList = new ArrayList<>();
    String triggeredButton = "Job 1";
    private HashMap<JButton, List<Integer>> buttonManager = new HashMap<>();

    private static final String[] headers =
            {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String JSON_STORE = "./data/schedule.json";

    // Modifies: this
    // Effects: Instantiates a new JFrame as well as some of the helper information needed
    // later on for saving/loading
    public ScheduleUI() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        frame = new JFrame("ScheduleApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 800);

//        JLabel background = new JLabel(new ImageIcon("./data/background.png"));
//        background.add(new JButton("Test"));

        // frame.add(background);

        setLandingContent(frame);
        // frame.pack();
        frame.setVisible(true);
    }

    // Requires: A valid (existing) JFrame to be passed
    // Modifies: this, frame
    // Effects: sets the landing page content with two interactive buttons and a
    // loaded-in background image
    // Attribution: image from reddit
    @SuppressWarnings("methodlength")
    private void setLandingContent(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        JLabel background = new JLabel(new ImageIcon("./data/background.png"));
        frame.setContentPane(background);
        frame.setLayout(new FlowLayout());

        topPanel = new JPanel();
        middlePanel = new JPanel();
        bottomPanel = new JPanel();

        newButton = new JButton("New Schedule");
        newButton.setPreferredSize(new Dimension(150, 50));
        loadButton = new JButton("Load Schedule");
        loadButton.setPreferredSize(new Dimension(150, 50));

        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                load = false;
                setViewContent(frame);
                setEditContent(frame);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                load = true;
                setViewContent(frame);
                setEditContent(frame);
            }
        });

        middlePanel.setLayout(new GridBagLayout());
        addComponent(middlePanel, newButton, 0, 0, 1, 1);
        addComponent(middlePanel, loadButton, 2, 0, 1, 1);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Requires: A valid (existing) JFrame to be passed
    // Modifies: this, frame
    // Effects: sets the editing page of the app to display the first job's schedule, any other
    // jobs, and add-job and save-jobs buttons
    @SuppressWarnings("methodlength")
    private void setEditContent(JFrame frame) {
        frame.getContentPane().removeAll();
        // display interactive schedule
        contentPane = new JPanel(new GridBagLayout());
        editScroll = new JPanel();
        JPanel panel = new JPanel(new GridBagLayout());
        headerLabel = new JLabel("Edit Schedule");
        GridBagConstraints gbc = new GridBagConstraints();
        HashMap<String, Boolean[][]> jobHourMap = new HashMap<>();
        HashMap<String, String[][]> viewableMap = new HashMap<>();
        java.util.List<String> jobList = new ArrayList<>();
        initializeDisplaySchedule(userSchedule, jobHourMap, viewableMap);
        gbc.gridx = 0;
        gbc.gridy = 0;

        for (String job : jobHourMap.keySet()) {
            editScroll.add(new JButton(new AbstractAction(job) {
                public void actionPerformed(ActionEvent e) {
                    JButton o = (JButton)e.getSource();
                    triggeredButton = o.getText();
                    setEditContent(frame);
                }
            }));
            jobList.add(job);
        }

        editScroll.add(new JButton(new AbstractAction("Add job") {
            public void actionPerformed(ActionEvent e) {
                userSchedule.addJob("Another Job", new HashMap<>());
                initializeDisplaySchedule(userSchedule, jobHourMap, viewableMap);
                frame.setVisible(true);
            }
        }));

        editScroll.add(new JButton(new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
                saveSchedule(userSchedule, jobList, jobHourMap);
                setLandingContent(frame);
            }
        }));

        contentPane.add(headerLabel, gbc);
        gbc.gridy = 1;
        contentPane.add(editScroll, gbc);
        JPanel junkPanel = new JPanel();
        for (String i : headers) {
            junkPanel.add(new JLabel(i));
        }
        gbc.gridy = 0;
        panel.add(junkPanel, gbc);

        int j = 1;
        for (List<JButton> bl : buttonGenerator(jobHourMap, triggeredButton, buttonManager)) {
            JPanel buttonPanel = new JPanel();

            for (int i = 0; i < 7; i++) {
                buttonPanel.add(bl.get(i));
            }
            gbc.gridy = j;
            j++;
            panel.add(buttonPanel, gbc);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        gbc.gridy = 2;
        contentPane.add(scrollPane, gbc);
        frame.getContentPane().add(contentPane);
        frame.setVisible(true);

    }

    // Requires: A valid (existing) JFrame to be passed
    // Modifies: this, frame
    // Effects: sets a viewing page that doesn't actually get used for UI in the final version,
    // just works to instantiate important fields
    @SuppressWarnings("methodlength")
    private void setViewContent(JFrame frame) {
        frame.getContentPane().removeAll();
        // Initialize and set some panel stuff
        contentPane = new JPanel(new GridBagLayout());
        editScroll = new JPanel();
        headerLabel = new JLabel("View Schedule");
        GridBagConstraints gbc = new GridBagConstraints();
        HashMap<String, Boolean[][]> jobHourMap = new HashMap<>();
        HashMap<String, String[][]> viewableMap = new HashMap<>();
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Initialize arrays for schedule tracking
        Boolean[][] dayData = new Boolean[24][7];
        String[][] dayDataViewable = new String[24][7];

        if (load) {
            jobList = new ArrayList<>();
            // load from json
            loadSchedule();
            // display schedule
            initializeDisplaySchedule(userSchedule, jobHourMap, viewableMap);

            for (String job : jobHourMap.keySet()) {
                editScroll.add(new JButton(job));
                jobList.add(job);
            }

            JTable table = new JTable(viewableMap.get(jobList.get(0)), headers);
            JScrollPane scrollableTable = new JScrollPane(table);
            scrollableTable.createVerticalScrollBar();
            contentPane.add(headerLabel, gbc);
            gbc.gridy = 1;
            contentPane.add(editScroll, gbc);
            gbc.gridy = 2;
            contentPane.add(scrollableTable, gbc);
            frame.getContentPane().add(contentPane);
        } else {
            userSchedule = new Schedule();
            initializeBlankArray(dayData, dayDataViewable);
            userSchedule.addJob("Job 1", new HashMap());

            for (int i = 1; i < 3; i++) {
                editScroll.add(new JButton("Job " + Integer.toString(i)));
            }

            JTable table = new JTable(dayDataViewable, headers);
            JScrollPane scrollableTable = new JScrollPane(table);
            scrollableTable.createVerticalScrollBar();
            contentPane.add(headerLabel, gbc);
            gbc.gridy = 1;
            contentPane.add(editScroll, gbc);
            gbc.gridy = 2;
            contentPane.add(scrollableTable, gbc);
            frame.getContentPane().add(contentPane);
        }
        frame.setVisible(true);
    }

    // Requires: A valid (existing) Schedule to be passed
    // Effects: Writes pertinent info about the schedule to a file to be loaded later
    private void saveSchedule(Schedule schedule,
                              java.util.List<String> jobList,
                              HashMap<String, Boolean[][]> jobHourMap) {
        Schedule freshSchedule = new Schedule();
        for (String jobName : jobList) {
            HashMap<String, java.util.List<Integer>> hours = new HashMap<>();
            for (int i = 0; i < 7; i++) {
                java.util.List<Integer> loj = new ArrayList<>();
                for (int j = 0; j < 24; j++) {
                    if (jobHourMap.get(jobName)[i][j].equals(true)) {
                        loj.add(j);
                    }
                }
                hours.put(headers[i], loj);
            }
            freshSchedule.addJob(jobName, hours);
        }
        schedule = freshSchedule;
        saveWrite(schedule);
    }

    // Requires: Valid (existing) Schedule
    // Effects: Writes a schedule to a JSON file
    private void saveWrite(Schedule userSchedule) {
        try {
            jsonWriter.open();
            jsonWriter.write(userSchedule);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Requires: HashMap<String, Boolean[][]> map, String jobName, HashMap<JButton, List<Integer>> buttonManager
    // Modifies: frame
    // Effects: Converts HashMap info about a job to a 2D list of buttons
    @SuppressWarnings("methodlength")
    private java.util.List<List<JButton>> buttonGenerator(HashMap<String, Boolean[][]> map,
                                                          String jobName,
                                                          HashMap<JButton, List<Integer>> buttonManager) {
        java.util.List<List<JButton>> yield = new ArrayList<>();
        List<JButton> temp = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 7; j++) {
                if (map.get(jobName)[j][i]) {
                    JButton tempButton = new JButton(new AbstractAction() { //"Booked",
                        public void actionPerformed(ActionEvent e) {
                            JButton o = (JButton)e.getSource();
                            switchText(o, map, jobName, buttonManager);
                            frame.getContentPane().repaint();
                            }
                        });
                    tempButton.setText("Booked");
                    temp.add(tempButton);
                    buttonManager.put(tempButton, Arrays.asList(i, j));
                } else {
                    JButton tempButton = new JButton(new AbstractAction() { //"Booked",
                        public void actionPerformed(ActionEvent e) {
                            JButton o = (JButton)e.getSource();
                            switchText(o, map, jobName, buttonManager);
                            frame.getContentPane().repaint();
                        }
                    });
                    tempButton.setText("-");
                    temp.add(tempButton);
                    buttonManager.put(tempButton, Arrays.asList(j, i));
                }
            }
            yield.add(temp);
            temp = new ArrayList<>();
        }
        return yield;
    }

    // Requires: JButton b, HashMap<String, Boolean[][]> map,
    // String jobName,
    // HashMap<JButton, List<Integer>> buttonManager
    // Modifies: JButton b, map, buttonManager
    // Effects: Switches a button from displaying "Booked" to "-" or vice versa as well its
    // representation in the tracking systems from busy to free or vice versa
    private void switchText(JButton b,
                            HashMap<String, Boolean[][]> map, String jobName,
                            HashMap<JButton, List<Integer>> buttonManager) {
        if (b.getText().equals("Booked")) {
            b.setText("-");
            int i = buttonManager.get(b).get(0);
            int j = buttonManager.get(b).get(1);
            map.get(jobName)[i][j] = false;
        } else {
            b.setText("Booked");
            int i = buttonManager.get(b).get(0);
            int j = buttonManager.get(b).get(1);
            map.get(jobName)[i][j] = true;
        }
    }

    // Requires: JButton b, HashMap<String, Boolean[][]> map,
    // String jobName,
    // HashMap<String, String[][]>> viewableMap
    // Modifies: schedule, jobHours, viewableMap
    // Effects: instantiates a display schedule when there is nothing yet loaded in
    private void initializeDisplaySchedule(Schedule schedule,
                                           HashMap<String, Boolean[][]> jobHours,
                                           HashMap<String, String[][]> viewableMap) {
        if (schedule.getJobList().isEmpty()) {
            schedule.addJob("Change me", new HashMap<>());
        } else {
            for (Job job : schedule.getJobList()) {
                jobHours.put(job.getName(), getJobHours(job.getSchedJobHours()));
                viewableMap.put(job.getName(), getViewableJobHours(job.getSchedJobHours()));
            }
        }
    }

    // Requires: List<List<Boolean>> hours
    // Effects: Returns an array representation of a 2D list of bools
    private Boolean[][] getJobHours(java.util.List<java.util.List<Boolean>> hours) {
        Boolean[][] boolHours = new Boolean[7][24];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                Array.set(boolHours[i], j, hours.get(i).get(j));
            }
        }
        return boolHours;
    }

    // Requires: List<List<Boolean>> hours
    // Effects: returns an array representation of the labels of a 2D list of bools
    private String[][] getViewableJobHours(java.util.List<java.util.List<Boolean>> hours) {
        String[][] stringHours = new String[7][24];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                if (hours.get(i).get(j)) {
                    Array.set(stringHours[i], j, "Booked");
                } else {
                    Array.set(stringHours[i], j, "-");
                }
            }
        }
        return stringHours;
    }

    // Requires: Boolean[][] dayData, String[][] dayDataViewable
    // Modifies: dayData, dayDataViewable
    // Effects:  initializes the above arrays to all free
    private void initializeBlankArray(Boolean[][] dayData, String[][] dayDataViewable) {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 7; j++) {
                Array.set(dayData[i], j, false);
                Array.set(dayDataViewable[i], j, "-");
            }
        }
    }

    // Requires: JPanel p, JComponent comp, int x, int y, int width, int height
    // Modifies: p
    // Effects: adds component comp to panel p
    // Inspiration from: https://www.section.io/engineering-education/working-with-gridbag-layout-in-java/
    private void addComponent(JPanel p, JComponent comp, int x, int y, int width, int height) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.gridx = x;
        constr.gridy = y;
        constr.gridheight = height;
        constr.gridwidth = width;
        constr.weightx = 2.0;
        constr.weighty = 0;
        constr.insets = new Insets(1, 1, 1, 1);
        constr.anchor = GridBagConstraints.CENTER;
        constr.fill = GridBagConstraints.BOTH;
        p.add(comp, constr);
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
}
