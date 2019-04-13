package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ConfigEntry {

    private final String key;
    @Setter(AccessLevel.PACKAGE) private Object value;

    public <T> T getValue(Class<? extends T> clazz) {
        return TransformerManager.getInstance().transform(value, clazz);
    }

}
