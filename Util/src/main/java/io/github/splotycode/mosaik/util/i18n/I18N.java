package io.github.splotycode.mosaik.util.i18n;

import io.github.splotycode.mosaik.util.io.IOUtil;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.HashMap;

public class I18N {

    @Getter private HashMap<String, String> map = new HashMap<>();
    private ThreadLocal<MessageFormat> formatThreadLocal = ThreadLocal.withInitial(() -> new MessageFormat(""));
    @Setter private I18N fallback;

    protected String getTranslation(String key) {
        String value = map.get(key);
        if (value == null && fallback != null) {
            value = fallback.map.get(key);
        }
        return value;
    }

    protected String getTranslation(String key, String def) {
        String translation = getTranslation(key);
        return translation == null ? def : translation;
    }

    public String get(String key, Object... objects) {
        String value = getTranslation(key);
        if (value == null) {
            System.err.println("Failed to find " + key + " in Locale");
            value = "I18N Error";
            try {
                new Throwable().printStackTrace();
            } catch (Throwable ignored) {}
        }
        return format(value, objects);
    }

    public String getOrDefault(String key, String fallback, Object... objects) {
        return format(getTranslation(key, fallback), objects);
    }

    protected String format(String value, Object... objects) {
        if (value == null) return null;
        MessageFormat format = formatThreadLocal.get();
        format.applyPattern(value);
        return format.format(objects);
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
