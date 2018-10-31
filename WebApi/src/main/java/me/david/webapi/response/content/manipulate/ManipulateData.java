package me.david.webapi.response.content.manipulate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;

public class ManipulateData {

    private HashMap<String, ManipulateVariable> variables = new HashMap<>();
    private HashMap<String, ManipulatePattern> patterns = new HashMap<>();

    public ManipulateVariable getVariable(String name) {
        return variables.get(name);
    }

    public ManipulatePattern getPattern(String name) {
        return patterns.get(name);
    }

    public ManipulateData(String input) {
        int state = 0;
        String stack = "";
        int start = -1;
        int current = 0;
        ManipulatePattern currentPattern = null;
        for (char ch : input.toCharArray()) {
            switch (state) {
                case 0:
                    if (ch == '$') {
                        state = 1;
                        start = current;
                    }
                    break;
                case 1:
                    if (stack.isEmpty() && ch == '@') {
                        state = 2;
                        start = current;
                    } else if (ch == '$') {
                        if (currentPattern == null) {
                            variables.put(stack, new ManipulateVariable(start, current));
                        } else {
                            currentPattern.variables.put(stack, new ManipulateVariable(start, current));
                        }
                        stack = "";
                        state = 0;
                    } else {
                        stack += ch;
                    }
                    break;
                case 2:
                    if (stack.isEmpty() && ch == '@') {
                        state = 3;
                    } else if (ch == '$') {
                        currentPattern = new ManipulatePattern();
                        patterns.put(stack, currentPattern);
                        stack = "";
                        state = 0;
                    } else {
                        stack += ch;
                    }
                    break;
                case 3:
                    if (ch == '$') {
                        currentPattern = null;
                        stack = "";
                        state = 0;
                    } else throw new SyntaxException("Expected $ for pattern close");
                    break;
            }
            current++;
        }
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class ManipulateVariable {

        private int start;
        private int end;

        public int getLenght() {
            return end - start;
        }

    }

    @Getter
    public static class ManipulatePattern {

        private HashMap<String, ManipulateVariable> variables = new HashMap<>();

    }
}
