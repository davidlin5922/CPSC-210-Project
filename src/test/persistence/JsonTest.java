package persistence;

import model.Component;
import model.Course;
import model.Mark;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class is to aid testing JsonReader and JsonWriter class in persistence package.
// The code in this class is adapted from JsonSerializationDemo provided by CPSC 210 course.

public class JsonTest {
    protected void checkCourse(String courseName, LinkedList<Component> marking,
                               LinkedList<Mark> marks, Course course) {
        assertEquals(courseName, course.getCourseName());
        for (int i=0 ; i < course.getMarkingScheme().size() ; i++) {
            assertEquals(marking.get(i).getComponentName(), course.getMarkingScheme().get(i).getComponentName());
            assertEquals(marking.get(i).getComponentWeight(), course.getMarkingScheme().get(i).getComponentWeight());
        }
        for (int i=0 ; i < course.getMarks().size() ; i++) {
            assertEquals(marks.get(i).getMark(), course.getMarks().get(i).getMark());
            assertEquals(marks.get(i).getName(), course.getMarks().get(i).getName());
        }
    }
}