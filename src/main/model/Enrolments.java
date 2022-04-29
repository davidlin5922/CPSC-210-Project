package model;

// This class represent the list of courses the user enrols in such as {CPSC 210, CPSC 121, ...}.
// A Enrolment has a list of course (eg. {courseA, courseB, ...}).

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

public class Enrolments implements Writable {

    private LinkedList<Course> enrolments;

    //EFFECT: create a new enrolment instance
    public Enrolments() {
        enrolments = new LinkedList<>();
    }

    //MODIFIES: this
    //EFFECT: add the course to the enrolments
    public void addCourse(Course courseName) {
        enrolments.add(courseName);
    }

    //MODIFIES: this
    //EFFECT: remove the course to the enrolments
    public void removeCourse(Course courseName) {
        enrolments.remove(courseName);
    }

    //EFFECT: return the list of courses
    public LinkedList<Course> getCourseList() {
        return enrolments;
    }

    //EFFECT: return the number of courses in the enrolments
    public int getEnrolmentSize() {
        return enrolments.size();
    }

    @Override
    // EFFECT: implement Writable interface to write in Enrolments to file
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("courseList", courseToJson());
        return json;
    }

    // EFFECT: return courses in this Enrolments as a JSON array
    private JSONArray courseToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Course c : enrolments) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }
}
