package io.github.splotycode.mosaik.argparserimpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsedArguments {

    private Map<String, String> arguments = new HashMap<>();

    public String getByKey(String key) {
        return arguments.get(key);
    }

    public Map<String, String> getArgumentMap() {
        return arguments;
    }

    public static ParsedArguments parse(String[] args) {
        ParsedArguments argument = new ParsedArguments();

        String key = null;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (key != null) {
                    argument.arguments.put(key, "_no_value_");
                }
                key = arg.substring(1);
            } else {
                if (key == null) throw new ArgParseException("Could not find key for value " + arg);
                argument.arguments.put(key, arg);
                key = null;
            }
        }
        return argument;
    }

}
