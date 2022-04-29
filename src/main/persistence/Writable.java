package persistence;

import org.json.JSONObject;

// This interface is implemented by the classes in the model package to write in data.
// The code in this interface is based on JsonSerializationDemo provided by CPSC 210 course.

public interface       Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
