package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import com.google.common.collect.Lists;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternAction;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternCommand;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternNotFoundException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class StringManipulator implements ResponseManipulator<StringManipulator> {

    private ManipulateData processedData;
    private String input;
    private Set<Replacement> replacements = new HashSet<>();

    private boolean cashing;

    public ManipulateData getManipulateData() {
        if (processedData == null) {
            processedData = ManipulateData.create(input, cashing);
        }
        return processedData;
    }

    @Override
    public StringManipulator setCashing(boolean cashing) {
        this.cashing = cashing;
        return this;
    }

    public StringManipulator(String input) {
        this.input = input;
    }

    @Override
    public StringManipulator variable(String str, Object obj) {
        List<ManipulateData.ManipulateVariable> variables = getManipulateData().getVariables(str);
        if (variables == null) throw new VariableNotFoundException("Could not find " + str);

        for (ManipulateData.ManipulateVariable variable : variables) {
            replacements.add(new Replacement(variable.getStart(), variable.getEnd(), obj.toString(), "replace normal variable"));
        }
        return this;
    }

    @Override
    public StringManipulator object(Object object) {
        ManipulateObjectAnalyser.AnalysedObject data = ManipulateObjectAnalyser.getObject(object);
        for (Map.Entry<String, List<ManipulateData.ManipulateVariable>> variable : getManipulateData().getVariableMap().entrySet()) {
            String name = variable.getKey();
            try {
                Object rawValue = data.getValueByName(object, name);
                String value = rawValue == null ? "null" : LinkBase.getInstance().getLink(TransformerManager.LINK).transform(rawValue, String.class);

                for (ManipulateData.ManipulateVariable rep : variable.getValue()) {
                    replacements.add(new Replacement(rep.getStart(), rep.getEnd(), value, "replace object variable"));
                }
            } catch (ManipulationException ignore) {

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ManipulationException("On " + object.getClass().getName() + "#" + name, e);
            }

        }
        return this;
    }

    private ManipulateData.ManipulatePattern patternFromName(String name) {
        ManipulateData.ManipulatePattern pattern = getManipulateData().getPattern(name);
        if (pattern == null) throw new PatternNotFoundException("Could not find " + name);
        return pattern;
    }

    private void doPattern(String name, boolean needFind, Function<String, Object> valueFunc) {
        ManipulateData.ManipulatePattern pattern = patternFromName(name);

        String result = applyReplacements(createPatternReplacements(pattern, needFind, valueFunc), pattern.getContent());
        replacements.add(new Replacement(pattern.getStart(), pattern.getStart(), result, "replace pattern instance: " + name));
    }

    private Set<Replacement> createPatternReplacements(ManipulateData.ManipulatePattern pattern, boolean needFind, Function<String, Object> valueFunc) {
        Set<Replacement> repVars = new HashSet<>();

        for (Map.Entry<String, List<ManipulateData.ManipulateVariable>> varibles : pattern.getVariables().entrySet()) {
            try {
                String varName = varibles.getKey();
                Object varRawValue = valueFunc.apply(varName);
                String varValue = varRawValue == null ? "null" : LinkBase.getInstance().getLink(TransformerManager.LINK).transform(varRawValue, String.class);
                for (ManipulateData.ManipulateVariable variable : varibles.getValue()) {
                    repVars.add(new Replacement(variable.getStart(), variable.getEnd(), varValue, "createPatternReplacements: " + pattern.getName() + " var: " + varName));
                }
            } catch (ManipulationException e){
                if (needFind) {
                    throw new ManipulationException("Failed to find value by key: " + varibles.getKey(), e);
                }
            }
        }

        return repVars;
    }

    @Override
    public StringManipulator pattern(String name, Object object) {
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
    public StringManipulator pattern(Object object) {
        pattern(object.getClass().getSimpleName().toLowerCase(), object);
        return this;
    }

    @Override
    public StringManipulator pattern(PatternCommand command) {
        handlePatternCommand(command, replacements);
        return this;
    }

    private void handlePatternCommand(PatternCommand command, Set<Replacement> replacements) {
        List<PatternAction> actions = new ArrayList<>();
        if (command.getPrimary() != null) {
            actions.add(command.getPrimary());
        }
        actions.addAll(command.getSecondaries());

        ManipulateData.ManipulatePattern cmdPatt = findPattern(command);

        if (replacements != this.replacements) {
            replacements.add(new Replacement(cmdPatt.getStart(), cmdPatt.getEnd(), "", "remove sub pattern"));
        }

        for (PatternAction action : actions) {
            String str = patternAction(action, cmdPatt);
            replacements.add(new Replacement(cmdPatt.getStart(), cmdPatt.getStart(), str, "replace sub pattern instance"));
        }
    }

    private ManipulateData.ManipulatePattern findPattern(PatternAction action) {
        return findPattern(action.getCommand());
    }

    private ManipulateData.ManipulatePattern findPattern(PatternCommand command) {
        List<PatternCommand> parents = new ArrayList<>();
        while (command.getParent() != null) {
            parents.add(command);
            command = command.getParent();
        }

        ManipulateData.ManipulatePattern pattern = patternFromName(command.getName());
        for (PatternCommand parent : Lists.reverse(parents)) {
            pattern = pattern.getChilds().get(parent.getName());
        }
        return pattern;
    }

    private String patternAction(PatternAction action, ManipulateData.ManipulatePattern pattern) {
        HashSet<Replacement> repl = new HashSet<>();

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
            handlePatternCommand(command, repl);
        }

        return applyReplacements(repl, pattern.getContent());
    }

    @Override
    public StringManipulator patternListName(String name, Iterable<?> objects) {
        objects.forEach(o -> pattern(name, o));
        return this;
    }

    @Override
    public StringManipulator patternList(Iterable<?> objects) {
        objects.forEach(this::pattern);
        return this;
    }

    @Override
    public StringManipulator patternArrayName(String name, Object... objects) {
        Arrays.stream(objects).forEach(o -> pattern(name, o));
        return this;
    }

    @Override
    public StringManipulator patternArray(Object... objects) {
        Arrays.stream(objects).forEach(this::pattern);
        return this;
    }

    @SafeVarargs
    @Override
    public final StringManipulator patternCostomName(String name, Pair<String, Object>... values) {
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
    public final StringManipulator patternCostomWithObj(String name, Object main, Pair<String, Object>... values) {
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
    public final StringManipulator patternCostomWithObj(Object main, Pair<String, Object>... values) {
        return patternCostomWithObj(main.getClass().getSimpleName(), main, values);
    }

    public void reset() {
        replacements.clear();
    }

    public void collectAllPatterns(Collection<ManipulateData.ManipulatePattern> patterns, ManipulateData.ManipulatePattern pattern) {
        for (ManipulateData.ManipulatePattern sub : pattern.getChilds().values()) {
            collectAllPatterns(patterns, sub);
        }
        patterns.add(pattern);
    }

    public Collection<ManipulateData.ManipulatePattern> collectAllPatterns() {
        List<ManipulateData.ManipulatePattern> collection = new ArrayList<>();
        for (ManipulateData.ManipulatePattern pattern : getManipulateData().getPatternMap().values()) {
            collectAllPatterns(collection, pattern);
        }
        return collection;
    }

    public String getResult() {
        for (ManipulateData.ManipulatePattern pattern : getManipulateData().getPatterns()) {
            replacements.add(new Replacement(pattern.getAbsoulteStart(), pattern.getAbsoulteEnd(), "", "remove pattern template"));
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
    @Getter
    public static class Replacement {

        private int start, end;
        private String content;
        @Setter private String note;

        public Replacement(int start, int end, String content) {
            this.start = start;
            this.end = end;
            this.content = content;
        }

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
