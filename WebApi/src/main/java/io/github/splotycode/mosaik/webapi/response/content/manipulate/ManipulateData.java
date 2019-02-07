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
    public HashMap<String, List<ManipulateVariable>> getVariableMap() {
        return variables;
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
        int varStartForPattern = -1;
        int current = 0;
       // String patternContent = null;
        ManipulatePattern currentPattern = null;
        for (char ch : input.toCharArray()) {
            ManipulatePattern upper = currentPattern;
            while (upper != null) {
                upper.setContent((upper.getContent() == null ? "" : upper.getContent()) + ch);
                upper = upper.parent;
            }
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
                            addVariable(currentPattern.variables, stack, new ManipulateVariable(start - varStartForPattern - 1, current - varStartForPattern));
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
                        ManipulatePattern parent = currentPattern;
                        currentPattern = new ManipulatePattern(start, parent);
                        if (parent != null) {
                            parent.getChilds().put(stack, currentPattern);
                        } else {
                            patterns.put(stack, currentPattern);
                        }
                        varStartForPattern = current;
                        stack = "";
                        state = 0;
                    } else {
                        stack += ch;
                    }
                    break;
                case 3:
                    if (ch == '$') {
                        if (currentPattern == null) throw new SyntaxException("Can not close Pattern if there was no pattern opened");
                        currentPattern.setEnd(current + 1);
                        currentPattern.setContent(StringUtil.removeLast(currentPattern.getContent(), 4));
                        currentPattern = currentPattern.parent;
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
        private HashMap<String, ManipulatePattern> childs = new HashMap<>();
        private ManipulatePattern parent;

        public ManipulatePattern(int start, ManipulatePattern parent) {
            this.start = start;
            this.parent = parent;
        }

        private HashMap<String, List<ManipulateVariable>> variables = new HashMap<>();

    }
}
