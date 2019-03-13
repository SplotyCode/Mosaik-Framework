package io.github.splotycode.mosaik.util.i18n;

import io.github.splotycode.mosaik.util.io.IOUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18N {

    private HashMap<String, String> map = new HashMap<>();

    public String get(String key, String... objects) {
        String value = map.get(key);
        if (value == null) {
            System.err.println("Failed to find " + key + " in english Locale");
            value = "I18N Error";
            try {
                new Throwable().printStackTrace();
            } catch (Throwable ex) {}
        }
        return MessageFormat.format(value, (Object[]) objects);
    }

    public I18N setLocale(ILocale locale) {
        map.clear();
        for (String line : IOUtil.loadLines(locale.getInputStream())) {
            if (line.startsWith("#") || line.isEmpty()) continue;
            String[] split = line.split(": ");
            map.put(split[0], split[1]);
        }
        return this;
    }

}
