package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class is for testing Enrolments class in model package.

public class EnrolmentsTest {

    Enrolments test;
    Course testCourse;

    @BeforeEach
    void setup() {
        test = new Enrolments();
        testCourse = new Course("CPSC 000");
    }

    @Test
    void testAddCourse() {
        test.addCourse(testCourse);
        assertEquals(1, test.getEnrolmentSize());
    }

    @Test
    void testRemoveCourse() {
        test.addCourse(testCourse);
        test.removeCourse(testCourse);
        assertEquals(0, test.getEnrolmentSize());
    }

    @Test
    void testGetCourseList() {
        test.addCourse(testCourse);
        assertEquals(testCourse, test.getCourseList().getFirst());
    }

}