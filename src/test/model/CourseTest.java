package model;

import exceptions.InvalidMark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// This class is for testing Course class in model package.

public class CourseTest {
    Course testCourse;
    Mark testMark;
    Mark projectMark;
    Component testComponent;
    Component project;

    @BeforeEach
    void setup() {
        testCourse = new Course("CPSC 000");
        testComponent = new Component("Exam", 50);
        project = new Component("Project", 20);
        testMark = new Mark(testComponent, "Final", 80.0);
        projectMark = new Mark(project, "Final Project", 100.0);
        try {
            testCourse.addMark(testMark);
        } catch (InvalidMark invalidMark) {
            fail("No error should be present");
        }
    }

    @Test
    void testAddComponent() {
        testCourse.addComponent(testComponent);
        assertEquals(1, testCourse.getMarkingScheme().size());
    }

    @Test
    void testAdjustComponent() {
        testCourse.addComponent(testComponent);
        testCourse.adjustComponent(testComponent, 60);
        assertEquals(60, testCourse.retrieveComponent(testComponent).getComponentWeight());
    }

    @Test
    void testRemoveComponent() {
        testCourse.addComponent(testComponent);
        assertEquals(1, testCourse.getMarkingScheme().size());
        testCourse.removeComponent(testComponent);
        assertEquals(0, testCourse.getMarkingScheme().size());
    }

    @Test
    void testRetrieveComponent() {
        assertNull(testCourse.retrieveComponent(testComponent));
        testCourse.addComponent(testComponent);
        assertEquals(testComponent, testCourse.retrieveComponent(testComponent));
    }

    @Test
    void testRetrieveComponentFail() {
        testCourse.addComponent(project);
        assertNull(testCourse.retrieveComponent(testComponent));
    }

    @Test
    void testAddMark() {

        assertEquals(1, testCourse.getMarks().size());
    }

    @Test
    void testAdjustMark() {

        testCourse.adjustMark(testMark, 85.0);
        assertEquals(85, testCourse.retrieveMark(testMark).getMark());
    }

    @Test
    void testRemoveMark() {

        assertEquals(1, testCourse.getMarks().size());
        testCourse.removeMark(testMark);
        assertEquals(0, testCourse.getMarks().size());
    }

    @Test
    void testRemoveMarkFail() {

        assertEquals(1, testCourse.getMarks().size());
        testCourse.removeMark(projectMark);
        assertEquals(1, testCourse.getMarks().size());
    }

    @Test
    void testRetrieveMark() {

        assertEquals(testMark, testCourse.retrieveMark(testMark));
    }

    @Test
    void testRetrieveMarkFail() {

        assertNull(testCourse.retrieveMark(projectMark));
    }

    @Test
    void testGetCourseName() {
        assertEquals("CPSC 000", testCourse.getCourseName());
    }

    @Test
    void testCalculateGrade() {
        testComponent.adjustWeighing(30);
        Component quiz = new Component("Quiz", 30);
        Component lab = new Component("Lab", 10);
        Component retrospective = new Component("Retrospective", 5);
        Component lectureTickets = new Component("LectureTickets", 5);
        testCourse.addComponent(quiz);
        testCourse.addComponent(lectureTickets);
        testCourse.addComponent(project);
        testCourse.addComponent(testComponent);
        testCourse.addComponent(lab);
        testCourse.addComponent(retrospective);
        Mark quiz1 = new Mark(quiz, "Quiz 1", 80.0);
        Mark quiz2 = new Mark(quiz, "Quiz 2", 58.4);
        Mark lab1 = new Mark(lab, "Lab 1", 100.0);
        Mark lab2 = new Mark(lab, "Lab 2", 100.0);
        Mark lab3 = new Mark(lab, "Lab 3", 83.3);
        Mark ticket1 = new Mark(lectureTickets, "Basic", 98.0);
        Mark ticket2 = new Mark(lectureTickets, "Abstraction", 55.0);
        Mark ticket3 = new Mark(lectureTickets, "Construction", 12.0);
        Mark retrospective1 = new Mark(retrospective, "First", 95.6);
        try {
            testCourse.addMark(ticket1);
            testCourse.addMark(quiz1);
            testCourse.addMark(retrospective1);
            testCourse.addMark(ticket2);
            testCourse.addMark(lab3);
            testCourse.addMark(ticket3);
            testCourse.addMark(lab1);
            testCourse.addMark(projectMark);
            testCourse.addMark(lab2);
            testCourse.addMark(quiz2);
            testMark.changeMark(88.88);
        } catch (InvalidMark invalidMark) {
            fail("No error should be present");
        }
        Double grade = 100.0 * 20 / 100 //project
                + 88.88 * 30 / 100 //exam
                + 95.6 * 5 / 100 //retrospective
                + (100.0 + 100.0 + 83.3) / 3 * 10 / 100 //lab
                + (80.0 + 58.4) / 2 * 30 / 100 //quiz
                + (98.0 + 55.0 + 12.0) / 3 * 5 / 100; //ticket

        assertEquals(grade, testCourse.calculateGrade());
    }

    @Test
    void testCalculateGradeEmptyComponent() {
        testComponent.adjustWeighing(30);
        Component quiz = new Component("Quiz", 30);
        Component lab = new Component("Lab", 10);
        Component retrospective = new Component("Retrospective", 5);
        Component lectureTickets = new Component("LectureTickets", 5);
        testCourse.addComponent(quiz);
        testCourse.addComponent(lectureTickets);
        testCourse.addComponent(project);
        testCourse.addComponent(testComponent);
        testCourse.addComponent(lab);
        testCourse.addComponent(retrospective);
        Mark quiz1 = new Mark(quiz, "Quiz 1", 80.0);
        Mark quiz2 = new Mark(quiz, "Quiz 2", 58.4);
        Mark ticket1 = new Mark(lectureTickets, "Basic", 98.0);
        Mark ticket2 = new Mark(lectureTickets, "Abstraction", 55.0);
        Mark ticket3 = new Mark(lectureTickets, "Construction", 12.0);
        Mark retrospective1 = new Mark(retrospective, "First", 95.6);
        try {
            testCourse.addMark(ticket1);
            testCourse.addMark(quiz1);
            testCourse.addMark(retrospective1);
            testCourse.addMark(ticket2);
            testCourse.addMark(ticket3);
            testCourse.addMark(projectMark);
            testCourse.addMark(quiz2);
            testMark.changeMark(88.88);
        } catch (InvalidMark invalidMark) {
            fail("No error should be present");
        }
        Double grade = 100.0 * 20 / 100 //project
                + 88.88 * 30 / 100 //exam
                + 95.6 * 5 / 100 //retrospective
                + (80.0 + 58.4) / 2 * 30 / 100 //quiz
                + (98.0 + 55.0 + 12.0) / 3 * 5 / 100; //ticket

        assertEquals(grade, testCourse.calculateGrade());
    }

    @Test
    void testCalculateGradeInvalidMark() {
        testComponent.adjustWeighing(30);
        Component quiz = new Component("Quiz", 30);
        Component lab = new Component("Lab", 10);
        Component retrospective = new Component("Retrospective", 5);
        Component lectureTickets = new Component("LectureTickets", 5);
        testCourse.addComponent(quiz);
        testCourse.addComponent(lectureTickets);
        testCourse.addComponent(project);
        testCourse.addComponent(testComponent);
        testCourse.addComponent(lab);
        testCourse.addComponent(retrospective);
        //Mark quiz1 = new Mark(quiz, "Quiz 1", 80.0);
        Mark quiz2 = new Mark(quiz, "Quiz 2", -58.4);
        Mark lab1 = new Mark(lab, "Lab 1", 100.0);
        Mark lab2 = new Mark(lab, "Lab 2", 100.0);
        Mark lab3 = new Mark(lab, "Lab 3", 83.3);
        Mark ticket1 = new Mark(lectureTickets, "Basic", 98.0);
        Mark ticket2 = new Mark(lectureTickets, "Abstraction", 55.0);
        Mark ticket3 = new Mark(lectureTickets, "Construction", 12.0);
        Mark retrospective1 = new Mark(retrospective, "First", 95.6);
        try {
            testCourse.addMark(ticket1);
            //testCourse.addMark(quiz1);
            testCourse.addMark(retrospective1);
            testCourse.addMark(ticket2);
            testCourse.addMark(lab3);
            testCourse.addMark(ticket3);
            testCourse.addMark(lab1);
            testCourse.addMark(projectMark);
            testCourse.addMark(lab2);
            testMark.changeMark(88.88);
        } catch (InvalidMark invalidMark) {

        }
        try {
            testCourse.addMark(quiz2);
            fail("Error is present");
        } catch (InvalidMark invalidMark) {

        }
        double grade = 100.0 * 20 / 100 //project
                + 88.88 * 30 / 100 //exam
                + 95.6 * 5 / 100 //retrospective
                + (100.0 + 100.0 + 83.3) / 3 * 10 / 100 //lab
                //+ (80.0 + 58.4 - 58.4) / 1 * 30 / 100 //quiz
                + (98.0 + 55.0 + 12.0) / 3 * 5 / 100; //ticket

        assertEquals(grade, testCourse.calculateGrade());
    }

    @Test
    void testCalculateGradeEmptyComponentInvalidMark() {
        testComponent.adjustWeighing(30);
        Component quiz = new Component("Quiz", 30);
        Component lab = new Component("Lab", 10);
        Component retrospective = new Component("Retrospective", 5);
        Component lectureTickets = new Component("LectureTickets", 5);
        testCourse.addComponent(quiz);
        testCourse.addComponent(lectureTickets);
        testCourse.addComponent(project);
        testCourse.addComponent(testComponent);
        testCourse.addComponent(lab);
        testCourse.addComponent(retrospective);
        Mark quiz1 = new Mark(quiz, "Quiz 1", 80.0);
        Mark quiz2 = new Mark(quiz, "Quiz 2", -58.4);
        Mark ticket1 = new Mark(lectureTickets, "Basic", 98.0);
        Mark ticket2 = new Mark(lectureTickets, "Abstraction", 55.0);
        Mark ticket3 = new Mark(lectureTickets, "Construction", 12.0);
        Mark retrospective1 = new Mark(retrospective, "First", 95.6);
        try {
            testCourse.addMark(ticket1);
            testCourse.addMark(quiz1);
            testCourse.addMark(retrospective1);
            testCourse.addMark(ticket2);
            testCourse.addMark(ticket3);
            testCourse.addMark(projectMark);
            testMark.changeMark(88.88);
        } catch (InvalidMark invalidMark) {
            fail("No error should be present");
        }
        try {
            testCourse.addMark(quiz2);
            fail("Error is present");
        } catch (InvalidMark invalidMark) {

        }
        Double grade = 100.0 * 20 / 100 //project
                + 88.88 * 30 / 100 //exam
                + 95.6 * 5 / 100 //retrospective
                + (80.0) / 1 * 30 / 100 //quiz
                + (98.0 + 55.0 + 12.0) / 3 * 5 / 100; //ticket

        assertEquals(grade, testCourse.calculateGrade());
    }
}