package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class is for testing Mark class in model package.

public class MarkTest {

    Mark testMark;
    Component testComponent;

    @BeforeEach
    void setup() {
        testMark = new Mark(testComponent, "Final", 80.0);
    }

    @Test
    void testChangeMark() {
        testMark.changeMark(85.0);
        assertEquals(85.0, testMark.getMark());
    }

    @Test
    void testGetName() {
        assertEquals("Final", testMark.getName());
    }
}