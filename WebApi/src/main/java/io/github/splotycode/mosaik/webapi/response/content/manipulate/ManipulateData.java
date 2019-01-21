package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.StringUtil;

import java.util.*;

public class ManipulateData {

    private HashMap<String, List<ManipulateVariable>> variables = new HashMap<>();
    private HashMap<String, ManipulatePattern> patterns = new HashMap<>();

    public Collection<ManipulatePattern> getPatterns() {
        return patterns.values();
    }

    public HashMap<String, ManipulatePattern> getPatternMap() {
        return patterns;
    }

    public List<ManipulateVariable> getVariables(String name) {
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
        String patternContent = null;
        ManipulatePattern currentPattern = null;
        for (char ch : input.toCharArray()) {
            if (patternContent != null) patternContent += ch;
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
                    } else if (ch == '$') {
                        if (currentPattern == null) {
                            addVariable(variables, stack, new ManipulateVariable(start, current + 1));
                        } else {
                            addVariable(currentPattern.variables, stack, new ManipulateVariable(start - currentPattern.start, current + 1 - currentPattern.start));
                        }
                        patternContent = "";
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
                        currentPattern = new ManipulatePattern(start);
                        patterns.put(stack, currentPattern);
                        stack = "";
                        state = 0;
                    } else {
                        stack += ch;
                    }
                    break;
                case 3:
                    if (ch == '$') {
                        if (currentPattern == null) throw new SyntaxException("Can not close Pattern if there was no pattern opened");
                        currentPattern.setEnd(current);
                        currentPattern.setContent(StringUtil.removeLast(patternContent, 4));
                        patternContent = null;
                        currentPattern = null;
                        stack = "";
                        state = 0;
                    } else throw new SyntaxException("Expected $ for pattern close");
                    break;
            }
            current++;
        }
    }

    private void addVariable(Map<String, List<ManipulateVariable>> map, String name, ManipulateVariable variable) {
        List<ManipulateVariable> list = map.computeIfAbsent(name, k -> new ArrayList<>());
        list.add(variable);
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

        private int start;
        @Setter private int end;
        @Setter private String content;

        public ManipulatePattern(int start) {
            this.start = start;
        }

        private HashMap<String, List<ManipulateVariable>> variables = new HashMap<>();

    }
}
