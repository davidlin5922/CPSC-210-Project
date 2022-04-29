package persistence;

import exceptions.InvalidMark;
import model.Component;
import model.Course;
import model.Enrolments;
import model.Mark;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// The code in this class is adapted from JsonSerializationDemo provided by CPSC 210 course.

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Enrolments from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Enrolments read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEnrolments(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses Enrolments from JSON object and returns it
    private Enrolments parseEnrolments(JSONObject jsonObject) {
        Enrolments enrol = new Enrolments();
        addCourses(enrol, jsonObject);
        return enrol;
    }

    // MODIFIES: enrol
    // EFFECTS: parses Courses from JSON object and adds them to Enrolments
    private void addCourses(Enrolments enrol, JSONObject jsonObject) {
        JSONArray courseArray = jsonObject.getJSONArray("courseList");
        for (Object courseJson : courseArray) {
            JSONObject nextCourse = (JSONObject) courseJson;
            addCourse(enrol, nextCourse);
        }
    }

    // MODIFIES: enrol
    // EFFECTS: parses Course from JSON object and adds it to Enrolments
    private void addCourse(Enrolments enrol, JSONObject jsonObject) {
        String courseName = jsonObject.getString("courseName");
        Course course = new Course(courseName);
        JSONArray componentArray = jsonObject.getJSONArray("componentList");
        for (Object componentJson : componentArray) {
            JSONObject nextComponent = (JSONObject) componentJson;
            addComponent(course, nextComponent);
        }
        JSONArray markArray = jsonObject.getJSONArray("markList");
        for (Object markJson : markArray) {
            JSONObject nextMark = (JSONObject) markJson;
            addMark(course, nextMark);
        }
        enrol.addCourse(course);
    }

    // MODIFIES: enrol
    // EFFECTS: parses Component from JSON object and adds it to Course
    private void addComponent(Course course, JSONObject componentJson) {
        String componentName = componentJson.getString("componentName");
        int componentWeight = componentJson.getInt("componentWeight");
        Component component = new Component(componentName, componentWeight);
        course.addComponent(component);
    }

    // MODIFIES: enrol
    // EFFECTS: parses Mark from JSON object and adds it to Course
    private void addMark(Course course, JSONObject markJson) {
        String componentName = markJson.getString("component");
        Component targetComponent = null;
        for (Component c : course.getMarkingScheme()) {
            if (c.getComponentName().equals(componentName)) {
                targetComponent = c;
            }
        }
        String markName = markJson.getString("markName");
        double mark = markJson.getDouble("mark");
        Mark newMark = new Mark(targetComponent, markName, mark);
        try {
            course.addMark(newMark);
        } catch (InvalidMark invalidMark) {
            // skip this mark
        }
    }
}
