package ui;

// This class is for the interactive console interface between the user and the program.

import exceptions.InvalidMark;
import model.Component;
import model.Course;
import model.Enrolments;
import model.Mark;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class StudentApp {
    private Enrolments enrolments;
    private Scanner scanner;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String DATA_LOCATION = "./data/enrolments.json";

    // EFFECT: construct Enrolments and run application
    public StudentApp() {
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter(DATA_LOCATION);
        jsonReader = new JsonReader(DATA_LOCATION);
        loadEnrolments();
        runProgram();
    }

    // MODIFIES: this
    // EFFECT: load the Enrolments from file, create a new Enrolments if does not exist
    private void loadEnrolments() {
        try {
            enrolments = jsonReader.read();
            System.out.println("Data loaded from " + DATA_LOCATION);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + DATA_LOCATION);
            System.out.println("New file created automatically!");
            enrolments = new Enrolments();
        }
    }

    // EFFECT: stop the program
    private void exit() {
        System.out.println("\nHave a nice day. Goodbye!");
        System.exit(0);
    }

    // EFFECT: save the Enrolments to file
    private void saveEnrolments() {
        try {
            jsonWriter.open();
            jsonWriter.write(enrolments);
            jsonWriter.close();
            System.out.println("All data saved to " + DATA_LOCATION);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + DATA_LOCATION);
        }
    }

    //MODIFIES: this
    //EFFECT: call methods according to user input, show main menu
    private void runProgram() {
        printMainMenu();
        int input = scanner.nextInt();
        if (input == 0) {
            saveEnrolments();
            exit();
        } else if (input == 1) {
            whichCourse(); //add mark
        } else if (input == 2) {
            removeMark(); //remove mark
        } else if (input == 3) {
            courseRelated(); // add, remove course
        } else if (input == 4) {
            changeWhichCourseGrading(); //add, change, show grading
        } else if (input == 5) {
            calculateGrade(); //calculate grade
        } else if (input == 6) {
            showMarks(); //show marks
        }
    }

    //EFFECT: give the options to add or remove a course
    private void courseRelated() {
        System.out.println("\nWhat would you like to do with course?");
        System.out.println("\t1. Add a course");
        System.out.println("\t2. Remove a course");
        int choose = scanner.nextInt();
        if (choose == 1) {
            addCourse();
        } else if (choose == 2) {
            removeCourse();
        }
    }

    //EFFECT: create a course instance and offer directions to adding components
    private void addCourse() {
        System.out.println("\nWhat is the course called (eg. CPSC 210)?");
        String newCourseName = scanner.next();
        newCourseName += scanner.nextLine();
        Course newCourse = new Course(newCourseName);
        enrolments.addCourse(newCourse);
        System.out.println("\nCourse added! Would you like to add a grading component now?");
        System.out.println("\t1. Yes");
        System.out.println("\t0. No");
        int addGradingSchemeNow = scanner.nextInt();
        if (addGradingSchemeNow == 0) {
            runProgram();
        } else if (addGradingSchemeNow == 1) {
            changeWhichCourseGrading();
        }
    }

    //EFFECT: let the user to specify which course to modify grading
    private void changeWhichCourseGrading() {
        System.out.println("\nWhich of the following course would you like to change the grading?");
        printCourseList();
        int courseSelection = scanner.nextInt();
        whatGradingChange(courseSelection);
    }

    //EFFECT: let the user to specify whether one would like to add/adjust/refer grading of the course
    private void whatGradingChange(int courseSelection) {
        String courseName = enrolments.getCourseList().get(courseSelection).getCourseName();
        System.out.println("\nWhat would you like to do to " + courseName + " grading?");
        System.out.println("\t1. Add a grading component");
        System.out.println("\t2. Adjust an existing grading component");
        //System.out.println("\t3. Remove an existing grading component");
        System.out.println("\t3. Show grading components of the course");
        int choose = scanner.nextInt();
        if (choose == 1) {
            addComponent(courseSelection);
        } else if (choose == 2) {
            changeComponent(courseSelection);
        } else if (choose == 3) {
            showGrading(courseSelection);
        }
    }

    //EFFECT: let the user to add a new grading component to the course
    private void addComponent(int courseSelection) {
        Course selectedCourse = enrolments.getCourseList().get(courseSelection);
        String selectedCourseName = selectedCourse.getCourseName();
        System.out.println("\nWhat is the new component called (eg. Lab, Midterm, Final, Reading, Homework)?");
        String newComponentName = scanner.next();
        newComponentName += scanner.nextLine();
        System.out.println("\nHow much does " + newComponentName
                + " weigh (eg. 25) in percentage to the overall course grade?");
        int newComponentWeight = scanner.nextInt();
        Component newComponent = new Component(newComponentName, newComponentWeight);
        selectedCourse.addComponent(newComponent);
        System.out.println("\nAdded a new component: " + newComponentName + ".");
        System.out.println("\nWould you like to add another component to " + selectedCourseName + " ?");
        System.out.println("\t1. Yes");
        System.out.println("\t0. No");
        int addGradingSchemeNow = scanner.nextInt();
        if (addGradingSchemeNow == 0) {
            runProgram();
        } else if (addGradingSchemeNow == 1) {
            addComponent(courseSelection);
        }
        runProgram();
    }

    //EFFECT: let the user to adjust the weight of a component in the course
    private void changeComponent(int courseSelection) {
        Course selectedCourse = enrolments.getCourseList().get(courseSelection);
        System.out.println("\nWhich component weighing would you like to modify?");
        printComponentList(courseSelection);
        int selectedComponent = scanner.nextInt();
        Component component = selectedCourse.getMarkingScheme().get(selectedComponent);
        String componentName = component.getComponentName();
        System.out.println("\nHow much does " + componentName
                + " now weigh (eg. 25) in percentage to the overall course grade?");
        int newWeight = scanner.nextInt();
        component.adjustWeighing(newWeight);
        System.out.println("\nModified the " + componentName + " component to " + newWeight + "%.");
        runProgram();
    }

    //EFFECT: let the user to remove a course from enrolments
    private void removeCourse() {
        System.out.println("\nWhich of the following courses do you want to remove?");
        System.out.println("Caution: All the marks and components associated with this course will be lost!");
        printCourseList();
        int courseSelection = scanner.nextInt();
        Course target = enrolments.getCourseList().get(courseSelection);
        String removedCourseName = target.getCourseName();
        enrolments.removeCourse(target);
        String removeMessage = "\n" + removedCourseName + " is removed!";
        System.out.println(removeMessage);
        runProgram();
    }

    //EFFECT: let the user to remove a specific mark from the course
    private void removeMark() {
        System.out.println("\nWhich of the following courses does this mark belong to?");
        printCourseList();
        int courseSelection = scanner.nextInt();
        Course course = enrolments.getCourseList().get(courseSelection);
        System.out.println("\nWhich of the following marks do you want to remove?");
        printMarks(courseSelection);
        int markSelection = scanner.nextInt();
        Mark selectedMark = course.getMarks().get(markSelection);
        course.removeMark(selectedMark);
        System.out.println("Removed mark " + selectedMark.getName());
        runProgram();
    }

    //EFFECT: when adding a new mark, let the user specify which course this mark belongs to
    private void whichCourse() {
        System.out.println("\nWhich of the following courses does this mark belong to?");
        printCourseList();
        int courseSelection = scanner.nextInt();
        whichComponent(courseSelection);
    }

    //EFFECT: when adding a new mark, let the user specify which component of the course this mark belongs to
    private void whichComponent(int courseSelection) {
        System.out.println("\nWhich of the following components does this mark belong to?");
        printComponentList(courseSelection);
        int selectedComponent = scanner.nextInt();
        inputMark(courseSelection, selectedComponent);
    }

    //EFFECT: create a new mark instance and add to the course
    private void inputMark(int courseSelection, int componentSelection) {
        Course course = enrolments.getCourseList().get(courseSelection);
        Component component = course.getMarkingScheme().get(componentSelection);
        System.out.println("\nPlease give this mark a name (eg. Unit 1 quiz).");
        String markName = scanner.next();
        markName += scanner.nextLine();
        System.out.println("\nPlease input your mark in percentage with at least one decimal place (eg. 95.0).");
        Double mark = scanner.nextDouble();
        Mark entry = new Mark(component, markName, mark);
        try {
            course.addMark(entry);
        } catch (InvalidMark invalidMark) {
            System.out.println("Invalid mark. Please try again.");
            inputMark(courseSelection, componentSelection);
        }
        String successAdd = "\nAdded a new " + component.getComponentName() + " entry " + mark + "%"
                + " to " + course.getCourseName();
        System.out.println(successAdd);
        afterMarkInput(courseSelection, componentSelection);
    }

    //EFFECT: let the user decide what to do after inputting a new mark
    private void afterMarkInput(int courseSelection, int componentSelection) {
        printKeepAdding();
        int choose = scanner.nextInt();
        if (choose == 1) {
            whichCourse();
        } else if (choose == 2) {
            whichComponent(courseSelection);
        } else if (choose == 3) {
            inputMark(courseSelection, componentSelection);
        } else if (choose == 0) {
            runProgram();
        }
    }

    //EFFECT: show the grading scheme of the course
    private void showGrading(int courseSelection) {
        LinkedList<Component> target = enrolments.getCourseList().get(courseSelection).getMarkingScheme();
        for (Component grading : target) {
            String part = "\t" + grading.getComponentName() + " = " + grading.getComponentWeight() + "%";
            System.out.println(part);
        }
        runProgram();
    }

    //EFFECT: calculate the course grade according to the components and marks recorded
    private void calculateGrade() {
        System.out.println("\nWhich of the following courses' grade would you like to calculate?");
        System.out.println("\nCaution: 0% will be given to the components that don't have any marks.");
        printCourseList();
        int courseSelection = scanner.nextInt();
        Course target = enrolments.getCourseList().get(courseSelection);
        String courseName = target.getCourseName();
        String grade = String.valueOf(target.calculateGrade());
        System.out.println("Your current grade in " + courseName + " is " + grade + " %.");
        runProgram();
    }

    //EFFECT: show all marks in the course
    private void showMarks() {
        System.out.println("\nWhich of the following courses' marks would you like to see?");
        printCourseList();
        int courseSelection = scanner.nextInt();
        printMarks(courseSelection);
        runProgram();
    }

    //EFFECT: print out all marks in the course for selection or display
    private void printMarks(int courseSelection) {
        LinkedList<Mark> target = enrolments.getCourseList().get(courseSelection).getMarks();
        for (int i = 0; i < target.size(); i++) {
            String createIndex = "\t" + i + ". "
                    + target.get(i).getName() + ": " + target.get(i).getMark() + "%";
            System.out.println(createIndex);
        }
    }

    //EFFECT: print out main menu options
    private void printMainMenu() {
        System.out.println("\nHello! Please select one of the following options by inputting the index.");
        System.out.println("\t1. Quick add a mark");
        System.out.println("\t2. Quick remove a mark");
        System.out.println("\t3. Add/Remove a course");
        System.out.println("\t4. Add/Change/Show course grading");
        System.out.println("\t5. Calculate a course grade");
        System.out.println("\t6. Show all the marks in a course");
        System.out.println("\t0. Save and Quit");
    }

    //EFFECT: print out all courses for selection
    private void printCourseList() {
        LinkedList<Course> courseList = enrolments.getCourseList();
        for (int i = 0; i < enrolments.getEnrolmentSize(); i++) {
            String createIndex = "\t" + i + ". " + courseList.get(i).getCourseName();
            System.out.println(createIndex);
        }
    }

    //EFFECT: print out all grading components in the course for selection or display
    private void printComponentList(int courseIndex) {
        LinkedList<Component> componentList = enrolments.getCourseList().get(courseIndex).getMarkingScheme();
        for (int i = 0; i < componentList.size(); i++) {
            String createIndex = "\t" + i + ". " + componentList.get(i).getComponentName();
            System.out.println(createIndex);
        }
    }

    //EFFECT: print out all options for the user to choose after inputting a mark
    private void printKeepAdding() {
        System.out.println("\nWhat would you like to do next?");
        System.out.println("\t1. Quick add a new mark in another course");
        System.out.println("\t2. Quick add a new mark in the same course");
        System.out.println("\t3. Quick add a new mark in the same course and the same component");
        System.out.println("\t0. Back to main menu");
    }
}