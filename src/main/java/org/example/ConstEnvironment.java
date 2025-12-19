package org.example;

import java.util.HashMap;
import java.util.Map;

public class ConstEnvironment {
    private final Map<String, Long> constants = new HashMap<>();

    public void define(String name, long value) {
        constants.put(name, value);
    }

    public long get(String name) {
        if (!constants.containsKey(name)) {
            throw new IllegalStateException("Undefined constant: " + name);
        }
        return constants.get(name);
    }
}


