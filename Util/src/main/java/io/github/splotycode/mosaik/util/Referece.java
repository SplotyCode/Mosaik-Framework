package io.github.splotycode.mosaik.util;

import io.github.splotycode.mosaik.util.listener.DefaultListenerHandler;
import io.github.splotycode.mosaik.util.listener.ValueChangeListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Referece<T> extends DefaultListenerHandler<ValueChangeListener<T>> {

    T value;

    public void setValue(T value) {
        call(listener -> listener.valueChange(Referece.this.value, value));
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
