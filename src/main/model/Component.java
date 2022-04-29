package model;

// This class represent the grading component of a course such as assignment, quiz, test, exam, tutorial, and lab.
// A component has a name (eg. "Project") and a weight (eg. 20) in percentage.

import org.json.JSONObject;
import persistence.Writable;

public class Component implements Writable {

    private String componentName;
    private int componentWeight;

    public Component(String name, int weight) {
        componentName = name;
        componentWeight = weight;
    }

    //REQUIRES: newWeight >=0
    //MODIFIES: this
    //EFFECT: change the weight of the component
    public void adjustWeighing(int newWeight) {
        componentWeight = newWeight;
    }

    //EFFECT: return the component name
    public String getComponentName() {
        return componentName;
    }

    //EFFECT: return the component weight
    public int getComponentWeight() {
        return componentWeight;
    }

    @Override
    // EFFECT: implement Writable interface to write in Component to file
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("componentName", componentName);
        json.put("componentWeight", componentWeight);
        return json;
    }
}
