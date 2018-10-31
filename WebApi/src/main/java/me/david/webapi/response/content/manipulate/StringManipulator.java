package me.david.webapi.response.content.manipulate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public class StringManipulator implements ResponseManipulator {

    private ManipulateData manipulateData;
    private String input;
    private Set<Replacement> replacements = new HashSet<>();

    @Override
    public ResponseManipulator variable(String str, Object obj) {
        ManipulateData.ManipulateVariable variable = manipulateData.getVariable(str);
        if (variable == null) throw new VariableNotFoundException("Could not find " + str);
        replacements.add(new Replacement(variable.getStart(), variable.getEnd(), obj.toString()));
        return this;
    }

    @Override
    public ResponseManipulator object(Object object) {
        MainpulateObjectAnalyser.AnalysedObject data = MainpulateObjectAnalyser.getObject(object);
        for (Map.Entry<String, String> entry : data.getFields().entrySet()) {
            ManipulateData.ManipulateVariable variable = manipulateData.getVariable(entry.getValue());
            if (variable != null) {
                try {
                    Field field = object.getClass().getField(entry.getKey());
                    field.setAccessible(true);
                    replacements.add(new Replacement(variable.getStart(), variable.getEnd(), field.get(object).toString()));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    @Override
    public ResponseManipulator pattern(String name, Object object) {
        ManipulateData.ManipulatePattern pattern = manipulateData.getPattern(name);
        if (pattern == null) throw new PatternNotFoundException("Could not find " + name);

        for (Map.Entry<String, String> entry : MainpulateObjectAnalyser.getObject(object).getFields().entrySet()) {
            ManipulateData.ManipulateVariable variable = pattern.getVariables().get(entry.getValue());
            if (variable != null) {
                try {
                    Field field = object.getClass().getField(entry.getKey());
                    field.setAccessible(true);
                    replacements.add(new Replacement(variable.getStart(), variable.getEnd(), field.get(object).toString()));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    @Override
    public ResponseManipulator pattern(Object object) {
        pattern(object.getClass().getName(), object);
        return this;
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

        public int lenghtDiff() {
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
