package io.battlesnake.starter;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SnakeTest {
    @Test
    void startTest() {
        Snake.Handler test = new Snake.Handler();
        Map bodyMap = new HashMap();
        Map<String, String> response = test.start(bodyMap);
        assertEquals("#ff00ff", response.get("color"));
    }

    @Test
    void moveTest() {
        Snake.Handler test = new Snake.Handler();
        Map bodyMap = new HashMap();
        Map<String, String> response = test.move(bodyMap);
        assertEquals("right", response.get("move"));
    }

    @Test
    void endTest() {
        Snake.Handler test = new Snake.Handler();
        Map bodyMap = new HashMap();
        Map<String, String> response = test.end(bodyMap);
        assertEquals(0, response.size());
    }
}