package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.util.prettyprint.IgnorePrint;
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
                        currentPattern = new ManipulatePattern(start, stack, parent);
                        if (parent != null) {
                            currentPattern.setStart(start - parent.getContentStart());
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
                        currentPattern.setEnd(currentPattern.getContentStart() + currentPattern.getContent().length());
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

    }

    @Getter
    public static class ManipulatePattern {

        @Setter private int start;
        @Setter private int end;
        @Setter private String content;
        private String name;

        private HashMap<String, ManipulatePattern> childs = new HashMap<>();
        @IgnorePrint private ManipulatePattern parent;

        public ManipulatePattern(int start, String name, ManipulatePattern parent) {
            this.start = start;
            this.name = name;
            this.parent = parent;
        }

        private HashMap<String, List<ManipulateVariable>> variables = new HashMap<>();

        public int getContentStart() {
            return 3 + name.length() + start;
        }

        public List<ManipulatePattern> getAllParents() {
            if(parent == null) return Collections.emptyList();
            List<ManipulatePattern> parents = new ArrayList<>();

            ManipulatePattern current = parent;
            while (current != null) {
                parents.add(current);
                current = current.getParent();
            }
            return parents;
        }

        public int getAbsoulteStart() {
            int prefix = 0;
            for (ManipulatePattern parent : getAllParents()) {
                prefix += parent.start;
            }
            return prefix + start;
        }

        public int getAbsoulteEnd() {
            int prefix = 0;
            for (ManipulatePattern parent : getAllParents()) {
                prefix += parent.end;
            }
            return prefix + end;
        }


    }
}
