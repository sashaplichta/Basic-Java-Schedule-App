# Job Manager
## Motivation
This project interests me because I am currently working a job between
classes, and one issue that I have found tedious to deal with ist that
of tracking hours. An application that allows me to easily tap in and
out of work would save me a great deal of acounting at the end of the
week. A relatively simple functionality addition is that of a tracker
by day of worked hours vs scheduled, as well as the surplus or deficit
accrued. Users could be anyone who has a schedule and wishes to track
whether they are keeping to this schedule.

In short, this application will be a "job manager" that allows a user to easily 
compare hours worked to scheduled hours for multiple jobs. It will be
geared towards university students and others who would want to budget 
time between different jobs/tasks.

## A Quick Note
The ScheduleApp class runs a console-based version of the app, while the 
ScheduleUI runs a GUI version of the app.

## Functionality
### A way to track job hours (*projected* and *actual*)

For example, a job landing page may contain:
- **Job 1** (5hr)
- **Job 2** (15hr)
- **Job 3** (35hr)

With each job linking to a corresponding page such as:

**Job X**
- Hours on *Monday*
- Hours on *Tuesday*
- ...

And for each job you should be able to "tap" in to and out of work
## User Stories
Examples Include:
- As a user, I want to add a job, with a schedule for hours per day
- As a user, I want to tap in/out of a job when I’m working
- As a user, I want to see a summary of my jobs at the end of the week
- As a user, I want to view my current jobs and hours worked

Data Persistence Stories:
- As a user, I want to be able to save my schedule
- As a user, I want to be able to load my schedule and interact with it

## Instructions for Grader
- You can generate the first required event related to adding hours to a job by:
  - Using your mouse, click on "New Schedule"
  - Click any of the buttons containing "-", they should say "Booked" after
  - Click on the button saying "Save", it should take you back to the landing page
- You can generate the second required event related to adding a job to the list of jobs by:
  - Click "Load Schedule", you should see the hours you just inputted
  - Click "Add Job"
  - Click "Job 1", you should see "Another Job" appear (sorry it's buggy)
  - Click "Another Job", you should see no hours booked
  - Click any hours (buttons with "-")
  - Click save, you should be able to load both jobs again if you wish. As a note, you can also remove any "scheduled" hours by clicking on them again which should return them to the "-" state from "Booked"
- You can locate the visual component by the pink-to-purple fade in the background
- You can save the state of the application using the "Save" button
- You can reload the state of the application using the "load" button
- 
