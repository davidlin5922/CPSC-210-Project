package persistence;

import exceptions.InvalidMark;
import model.Component;
import model.Course;
import model.Enrolments;
import model.Mark;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// This class is for testing JsonWriter class in persistence package.
// The code in this class is adapted from JsonSerializationDemo provided by CPSC 210 course.

public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyEnrolment() {
        try {
            Enrolments enrol = new Enrolments();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyEnrolments.json");
            writer.open();
            writer.write(enrol);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterEmptyEnrolments.json");
            enrol = reader.read();
            assertEquals(0, enrol.getEnrolmentSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralEnrolments() {
        try {
            Enrolments enrol = new Enrolments();
            Course test210 = new Course("CPSC 210");
            Course test121 = new Course("CPSC 121");
            enrol.addCourse(test210);
            enrol.addCourse(test121);
            Component quizComponent = new Component("Quiz", 30);
            Component finalComponent = new Component("Final", 35);
            Component tutorialComponent = new Component("Tutorial", 6);
            test210.addComponent(quizComponent);
            test210.addComponent(finalComponent);
            test121.addComponent(tutorialComponent);
            Mark quizMark = new Mark(quizComponent, "testQuiz", 80.3);
            Mark tutorialMark1 = new Mark(tutorialComponent, "testTutorial1", 100.0);
            Mark tutorialMark2 = new Mark(tutorialComponent, "testTutorial2", 100.0);
            try {
                test210.addMark(quizMark);
                test121.addMark(tutorialMark1);
                test121.addMark(tutorialMark2);
            } catch (InvalidMark invalidMark) {
                fail("No error should be present.");
            }
            LinkedList<Component> marking210 = test210.getMarkingScheme();
            LinkedList<Mark> mark210 = test210.getMarks();
            LinkedList<Component> marking121 = test121.getMarkingScheme();
            LinkedList<Mark> mark121 = test121.getMarks();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEnrolments.json");
            writer.open();
            writer.write(enrol);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterGeneralEnrolments.json");
            enrol = reader.read();
            LinkedList<Course> courses = enrol.getCourseList();
            assertEquals(2, courses.size());
            checkCourse("CPSC 210", marking210, mark210, enrol.getCourseList().get(0));
            checkCourse("CPSC 121", marking121, mark121, enrol.getCourseList().get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralEnrolmentsInvalidMark() {
        try {
            Enrolments enrol = new Enrolments();
            Course test210 = new Course("CPSC 210");
            Course test121 = new Course("CPSC 121");
            enrol.addCourse(test210);
            enrol.addCourse(test121);
            Component quizComponent = new Component("Quiz", 30);
            Component finalComponent = new Component("Final", 35);
            Component tutorialComponent = new Component("Tutorial", 6);
            test210.addComponent(quizComponent);
            test210.addComponent(finalComponent);
            test121.addComponent(tutorialComponent);
            Mark quizMark = new Mark(quizComponent, "testQuiz", 80.3);
            Mark tutorialMark1 = new Mark(tutorialComponent, "testTutorial1", 100.0);
            Mark tutorialMark2 = new Mark(tutorialComponent, "testTutorial2", -1.0);
            try {
                test210.addMark(quizMark);
                test121.addMark(tutorialMark1);
            } catch (InvalidMark invalidMark) {
                fail("No error should be present.");
            }
            try {
                test121.addMark(tutorialMark2);
                fail("Error is present");
            } catch (InvalidMark invalidMark) {

            }
            LinkedList<Component> marking210 = test210.getMarkingScheme();
            LinkedList<Mark> mark210 = test210.getMarks();
            LinkedList<Component> marking121 = test121.getMarkingScheme();
            LinkedList<Mark> mark121 = test121.getMarks();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEnrolmentsInvalidMark.json");
            writer.open();
            writer.write(enrol);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterGeneralEnrolmentsInvalidMark.json");
            enrol = reader.read();
            LinkedList<Course> courses = enrol.getCourseList();
            assertEquals(2, courses.size());
            checkCourse("CPSC 210", marking210, mark210, enrol.getCourseList().get(0));
            checkCourse("CPSC 121", marking121, mark121, enrol.getCourseList().get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}