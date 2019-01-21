package io.github.splotycode.mosaik.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.ValueChangeListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Referece<T> extends ListenerHandler<ValueChangeListener<T>> {

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
