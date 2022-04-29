package model;

// This class represent the course in enrolments such as CPSC 210 and CPSC 110.
// A component has a name (eg. "CPSC 121"), a marking scheme (eg. {componentA, componentB, ...}
// , and Marks (eg. {mark1, mark2, ...}.

import exceptions.InvalidMark;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

public class Course implements Writable {
    private String name;
    private LinkedList<Component> marking;
    private LinkedList<Mark> marks;

    public Course(String courseName) {
        name = courseName;
        marking = new LinkedList<>();
        marks = new LinkedList<>();
    }

    //REQUIRE: the same component not already in marking
    //MODIFIES: this
    //EFFECT: add a grading component for this course
    public void addComponent(Component c) {
        this.marking.add(c);
    }

    //MODIFIES: this
    //EFFECT: change the weighing of the grading component in this course
    public void adjustComponent(Component component, int newWeight) {
        retrieveComponent(component).adjustWeighing(newWeight);
    }

    //REQUIRES: marking not empty
    //MODIFIES: this
    //EFFECT: remove the grading component from the course
    public void removeComponent(Component target) {
        this.marking.remove(target);
    }

    //EFFECT: retrieve the component from the list of components
    public Component retrieveComponent(Component target) {
        Component componentFound = null;
        for (Component component : this.marking) {
            if (component == target) {
                componentFound = component;
            }
        }
        return componentFound;
    }

    //REQUIRES: mark >= 0, the exact same mark (type and name) not already in marks
    //MODIFIES: this
    //EFFECT: add a new mark entry
    public void addMark(Mark mark) throws InvalidMark {
        if (mark.getMark() < 0) {
            throw new InvalidMark();
        }
        this.marks.add(mark);
    }

    //MODIFIES: this
    //EFFECT: change a specific mark in this course
    public void adjustMark(Mark mark, Double newMark) {
        retrieveMark(mark).changeMark(newMark);
    }


    //REQUIRES: Marks not empty
    //MODIFIES: this
    //EFFECT: add a new mark entry
    public void removeMark(Mark mark) {
        for (Mark entry : this.marks) {
            if (mark == entry) {
                marks.remove(entry);
            }
        }
    }

    //EFFECT: retrieve the component from the list of components
    public Mark retrieveMark(Mark target) {
        Mark markFound = null;
        for (Mark mark : this.marks) {
            if (mark == target) {
                markFound = mark;
            }
        }
        return markFound;
    }

    //EFFECT: produce a list of component grades according to the component and mark
    //        assume zero for components with no marks
    public double calculateGrade() {
        LinkedList<Double> gradeCalc = new LinkedList<>();
        for (int i = 0; i < marking.size(); i++) {
            LinkedList<Mark> forSorting = new LinkedList<>();
            for (Mark mark : marks) {
                if (mark.getType() == marking.get(i)) {
                    forSorting.add(mark);
                }
            }
            double componentSum = 0.0;
            double componentFinal;
            if (forSorting.size() == 0) {
                componentFinal = 0.0;
            } else {
                for (Mark mark : forSorting) {
                    componentSum += mark.getMark();
                }
                componentFinal = (componentSum / forSorting.size()) * (marking.get(i).getComponentWeight() / 100.0);
            }
            gradeCalc.add(componentFinal);
        }
        return addUp(gradeCalc);
    }

    //EFFECT: add up the component grades to calculate the final course grade
    private double addUp(LinkedList<Double> gradeCalc) {
        double finalGrade = 0.0;
        for (double mark : gradeCalc) {
            finalGrade += mark;
        }
        return finalGrade;
    }

    //EFFECT: return the name of the course
    public String getCourseName() {
        return name;
    }

    //EFFECT: return the list of grading component of the course
    public LinkedList<Component> getMarkingScheme() {
        return marking;
    }

    //EFFECT: return the list of marks of the course
    public LinkedList<Mark> getMarks() {
        return marks;
    }

    @Override
    // EFFECT: implement Writable interface to write in Component to file
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("courseName", name);
        json.put("componentList", componentToJson());
        json.put("markList", markToJson());
        return json;
    }

    // EFFECT: return Components in this Course as a JSON array
    private JSONArray componentToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Component c : marking) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }

    // EFFECT: return Marks in this Course as a JSON array
    private JSONArray markToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Mark m : marks) {
            jsonArray.put(m.toJson());
        }
        return jsonArray;
    }
}
