package model;

// This class represent the mark of a course such as Quiz1, Final, Midterm, Project, Lab1, Lab2, ...
// A Mark has a type (eg. Tutorial), a name (eg. "Tutorial1"), and a mark (eg. 95.0) in percentage.

import org.json.JSONObject;
import persistence.Writable;


public class Mark implements Writable {

    private Component type;
    private String name;
    private double mark;

    public Mark(Component component, String name, Double mark) {
        this.type = component;
        this.name = name;
        this.mark = mark;
    }

    //EFFECT: return the type (component) of the mark
    public Component getType() {
        return type;
    }

    //EFFECT: return the name of the mark
    public String getName() {
        return name;
    }

    //EFFECT: return the mark
    public Double getMark() {
        return mark;
    }

    //MODIFIES: this
    //EFFECT: change the mark to newMark
    public void changeMark(Double newMark) {
        mark = newMark;
    }

    @Override
    // EFFECT: implement Writable interface to write in Mark to file
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("component", type.getComponentName());
        json.put("markName", name);
        json.put("mark", mark);
        return json;
    }
}
