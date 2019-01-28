package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class StringManipulator implements ResponseManipulator {

    private ManipulateData manipulateData;
    private String input;
    private Set<Replacement> replacements = new HashSet<>();

    public StringManipulator(String input) {
        this.input = input;
        manipulateData = new ManipulateData(input);
    }

    @Override
    public ResponseManipulator variable(String str, Object obj) {
        List<ManipulateData.ManipulateVariable> variables = manipulateData.getVariables(str);
        if (variables == null) throw new VariableNotFoundException("Could not find " + str);

        for (ManipulateData.ManipulateVariable variable : variables) {
            replacements.add(new Replacement(variable.getStart(), variable.getEnd(), obj.toString()));
        }
        return this;
    }

    @Override
    public ResponseManipulator object(Object object) {
        ManipulateObjectAnalyser.AnalysedObject data = ManipulateObjectAnalyser.getObject(object);
        for (Map.Entry<String, String> entry : data.getFields().entrySet()) {
            List<ManipulateData.ManipulateVariable> variables = manipulateData.getVariables(entry.getValue());
            if (variables != null) {
                Field field = null;
                try {
                    field = object.getClass().getField(entry.getKey());
                    field.setAccessible(true);
                    String value = field.get(object).toString();
                    for (ManipulateData.ManipulateVariable variable : variables) {
                        replacements.add(new Replacement(variable.getStart(), variable.getEnd(), value));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new ManipulationException("On " + object.getClass().getName() + "#" + (field == null ? "null" : field.getName()), e);
                }
            }
        }
        return this;
    }

    @Override
    public ResponseManipulator pattern(String name, Object object) {
        ManipulateData.ManipulatePattern pattern = manipulateData.getPattern(name);
        if (pattern == null) throw new PatternNotFoundException("Could not find " + name);

        Set<Replacement> repVars = new HashSet<>();

        for (Map.Entry<String, String> entry : ManipulateObjectAnalyser.getObject(object).getFields().entrySet()) {
            List<ManipulateData.ManipulateVariable> variables = pattern.getVariables().get(entry.getValue());
            if (variables != null) {
                Field field = null;
                try {
                    field = object.getClass().getField(entry.getKey());
                    field.setAccessible(true);
                    String value = field.get(object).toString();
                    for (ManipulateData.ManipulateVariable variable : variables) {
                        repVars.add(new Replacement(variable.getStart(), variable.getEnd(), value));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new ManipulationException("On " + object.getClass().getName() + "#" + (field == null ? "null" : field.getName()), e);
                }
            }
        }
        String result = applyReplacements(repVars, pattern.getContent());
        replacements.add(new Replacement(pattern.getStart(), pattern.getStart(), result));
        return this;
    }

    @Override
    public ResponseManipulator pattern(Object object) {
        pattern(object.getClass().getSimpleName().toLowerCase(), object);
        return this;
    }

    @Override
    public ResponseManipulator pattern(Iterable<?> objects) {
        objects.forEach(this::pattern);
        return this;
    }

    public String getResult() {
        for (ManipulateData.ManipulatePattern pattern : manipulateData.getPatternMap().values()) {
            replacements.add(new Replacement(pattern.getStart(), pattern.getEnd(), ""));
        }

        return applyReplacements(replacements, input);
    }

    private String applyReplacements(Set<Replacement> replacements, String str) {
        StringBuilder buffer = new StringBuilder(input);
        int delta = 0;
        for (Replacement replacement : replacements) {
            buffer.replace(replacement.start + delta, replacement.end + delta, replacement.content);
            delta += replacement.lengthDiff();
        }
        return buffer.toString();
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Replacement {

        private int start, end;
        private String content;

        public int beforeLength() {
            return end - start;
        }

        public int endLength() {
            return content.length();
        }

        public int lengthDiff() {
            return endLength() - beforeLength();
        }

    }

    public static class VariableNotFoundException extends RuntimeException {

        public VariableNotFoundException() {
        }

        public VariableNotFoundException(String s) {
            super(s);
        }

        public VariableNotFoundException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public VariableNotFoundException(Throwable throwable) {
            super(throwable);
        }

        public VariableNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

    public static class PatternNotFoundException extends RuntimeException {

        public PatternNotFoundException() {
        }

        public PatternNotFoundException(String s) {
            super(s);
        }

        public PatternNotFoundException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public PatternNotFoundException(Throwable throwable) {
            super(throwable);
        }

        public PatternNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }
}
