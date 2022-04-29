package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class is for testing Component class in model package.

public class ComponentTest {

    Component testComponent;

    @BeforeEach
    void setup() {
        testComponent = new Component("Exam", 50);
    }

    @Test
    void testAdjustWeighing() {
        testComponent.adjustWeighing(40);
        assertEquals(40, testComponent.getComponentWeight());
    }

    @Test
    void testGetComponentName() {
        assertEquals("Exam", testComponent.getComponentName());
    }
}