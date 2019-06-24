package io.github.splotycode.mosaik.util.i18n;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class MessageContext {

    protected I18N translator;
    protected Map<String, String> replacements = new HashMap<>();
    @Setter protected String prefix;
    @Setter protected boolean allLinePrefix;

    public MessageContext(I18N translator, String prefix, boolean allLinePrefix) {
        this.translator = translator;
        this.prefix = prefix;
        this.allLinePrefix = allLinePrefix;
    }

    protected RuntimeException getErrorException(String message) {
        return new RuntimeException(message);
    }

    public void throwErrorRaw(String text) {
        throw getErrorException(text);
    }

    public void throwError(String key, Object... objects) {
        throwErrorRaw(translate(key, objects));
    }

    public String transformRaw(String text) {
        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            text = text.replace(replacement.getKey(), replacement.getValue());
        }
        if (allLinePrefix) {
            StringBuilder builder = new StringBuilder();
            for (String line : text.split("\n")) {
                builder.append(prefix).append(line).append("\n");
            }
            return builder.toString();
        }
        return prefix + text;
    }

    public MessageContext addReplacement(String key, String value) {
        replacements.put(key, value);
        return this;
    }

    public String translate(String key, Object... objects) {
        return transformRaw(translator.get(key, objects));
    }

}
