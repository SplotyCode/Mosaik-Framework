package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternCommand;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternNotFoundException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        for (Map.Entry<String, List<ManipulateData.ManipulateVariable>> variable : manipulateData.getVariableMap().entrySet()) {
            String name = variable.getKey();
            try {
                Object rawValue = data.getValueByName(object, name);
                String value = rawValue == null ? "null" : LinkBase.getInstance().getLink(TransformerManager.LINK).transform(rawValue, String.class);

                for (ManipulateData.ManipulateVariable rep : variable.getValue()) {
                    replacements.add(new Replacement(rep.getStart(), rep.getEnd(), value));
                }
            } catch (ManipulationException ignore) {

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ManipulationException("On " + object.getClass().getName() + "#" + name, e);
            }

        }
        for (Map.Entry<String, Field> entry : data.getFields().entrySet()) {
            List<ManipulateData.ManipulateVariable> variables = manipulateData.getVariables(entry.getKey());
            Field field = entry.getValue();
            if (variables != null) {
                try {
                    field.setAccessible(true);
                    String value = field.get(object).toString();
                    for (ManipulateData.ManipulateVariable variable : variables) {
                        replacements.add(new Replacement(variable.getStart(), variable.getEnd(), value));
                    }
                } catch (IllegalAccessException e) {
                    throw new ManipulationException("On " + object.getClass().getName() + "#" + (field == null ? "null" : field.getName()), e);
                }
            }
        }
        return this;
    }

    private ManipulateData.ManipulatePattern patternFromName(String name) {
        ManipulateData.ManipulatePattern pattern = manipulateData.getPattern(name);
        if (pattern == null) throw new PatternNotFoundException("Could not find " + name);
        return pattern;
    }

    private void doPattern(String name, boolean needFind, Function<String, Object> valueFunc) {
        ManipulateData.ManipulatePattern pattern = patternFromName(name);

        String result = applyReplacements(createPatternReplacements(pattern, needFind, valueFunc), pattern.getContent());
        replacements.add(new Replacement(pattern.getStart(), pattern.getStart(), result));
    }

    private Set<Replacement> createPatternReplacements(ManipulateData.ManipulatePattern pattern, boolean needFind, Function<String, Object> valueFunc) {
        Set<Replacement> repVars = new HashSet<>();

        for (Map.Entry<String, List<ManipulateData.ManipulateVariable>> varibles : pattern.getVariables().entrySet()) {
            try {
                String varName = varibles.getKey();
                Object varRawValue = valueFunc.apply(varName);
                String varValue = varRawValue == null ? "null" : LinkBase.getInstance().getLink(TransformerManager.LINK).transform(varRawValue, String.class);
                for (ManipulateData.ManipulateVariable variable : varibles.getValue()) {
                    repVars.add(new Replacement(variable.getStart(), variable.getEnd(), varValue));
                }
            } catch (ManipulationException e){
                if (needFind) {
                    throw new ManipulationException("Failed to find value by key", e);
                }
            }
        }

        return repVars;
    }

    @Override
    public ResponseManipulator pattern(String name, Object object) {
        ManipulateObjectAnalyser.AnalysedObject analysedObject = ManipulateObjectAnalyser.getObject(object);
        doPattern(name, true, varName -> {
            try {
                return analysedObject.getValueByName(object, varName);
            } catch (ReflectiveOperationException e) {
                throw new ManipulationException("On " + object.getClass().getName() + "#" + varName, e);
            }
        });
        return this;
    }

    @Override
    public ResponseManipulator pattern(Object object) {
        pattern(object.getClass().getSimpleName().toLowerCase(), object);
        return this;
    }

    @Override
    public ResponseManipulator pattern(PatternCommand command) {
        //patternAction(command.getPrimary());
        //command.getSecondaries().forEach(this::patternAction);
        return this;
    }

    /*private Set<Replacement> commandToReplacements(PatternCommand command, Set<Replacement> replacements) {
        for (PatternCommand child : command.)
    }

    private void patternAction(PatternAction action) {
        Set<Replacement> repl = new HashSet<>();
        String patName = action.getCommand().getName();
        ManipulateData.ManipulatePattern pattern = patternFromName(patName);

        repl.addAll(createPatternReplacements(pattern, true, name -> {
            for (Object object : action.getObjects()) {
                try {
                    return ManipulateObjectAnalyser.getObject(object).getValueByName(object, name);
                } catch (ManipulationException ignore) {
                } catch (IllegalAccessException | InvocationTargetException e) {
                    ExceptionUtil.throwRuntime(e);
                }
            }
            Object object = action.getCostom().get(name);
            if (object == null) {
                throw new ManipulationException("No value found for variable " + name);
            }
            return object;
        }));
        for (PatternCommand command : action.getChilds()) {
            repl.addAll(commandToReplacements(command, new HashSet<>()));
        }


        String result = applyReplacements(repl, pattern.getContent());
        replacements.add(new Replacement(pattern.getStart(), pattern.getStart(), result));
    }*/

    @Override
    public ResponseManipulator patternListName(String name, Iterable<?> objects) {
        objects.forEach(o -> pattern(name, o));
        return this;
    }

    @Override
    public ResponseManipulator patternList(Iterable<?> objects) {
        objects.forEach(this::pattern);
        return this;
    }

    @Override
    public ResponseManipulator patternArrayName(String name, Object... objects) {
        Arrays.stream(objects).forEach(o -> pattern(name, o));
        return this;
    }

    @Override
    public ResponseManipulator patternArray(Object... objects) {
        Arrays.stream(objects).forEach(this::pattern);
        return this;
    }

    @SafeVarargs
    @Override
    public final ResponseManipulator patternCostomName(String name, Pair<String, Object>... values) {
        Map<String, Object> valueMap = CollectionUtil.newHashMap(values);
        doPattern(name, true, varName -> {
            Object value = valueMap.get(varName);
            if (value == null) {
                throw new ManipulationException("No value found for variable " + varName);
            }
            return value;
        });
        return this;
    }

    @SafeVarargs
    @Override
    public final ResponseManipulator patternCostomWithObj(String name, Object main, Pair<String, Object>... values) {
        Map<String, Object> valueMap = CollectionUtil.newHashMap(values);
        ManipulateObjectAnalyser.AnalysedObject analysedObject = ManipulateObjectAnalyser.getObject(main);
        doPattern(name, true, varName -> {
            try {
                return analysedObject.getValueByName(main, varName);
            } catch (ReflectiveOperationException e) {
                throw new ManipulationException("On " + main.getClass().getName() + "#" + varName, e);
            } catch (ManipulationException e) {
                Object obj = valueMap.get(varName);
                if (obj == null) {
                    throw new ManipulationException("Variable not found in costom or in main obect", e);
                }
                return obj;
            }
        });
        return this;
    }

    @SafeVarargs
    @Override
    public final ResponseManipulator patternCostomWithObj(Object main, Pair<String, Object>... values) {
        return patternCostomWithObj(main.getClass().getSimpleName(), main, values);
    }

    public void reset() {
        replacements.clear();
    }

    public String getResult() {
        for (ManipulateData.ManipulatePattern pattern : manipulateData.getPatternMap().values()) {
            replacements.add(new Replacement(pattern.getStart(), pattern.getEnd(), ""));
        }

        return applyReplacements(replacements, input);
    }

    private String applyReplacements(Set<Replacement> replacements, String str) {
        StringBuilder buffer = new StringBuilder(str);
        int delta = 0;
        for (Replacement replacement : replacements.stream().sorted(Comparator.comparingInt(replacement -> replacement.end)).collect(Collectors.toList())) {
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

}
