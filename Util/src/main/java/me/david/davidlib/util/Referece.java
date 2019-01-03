package me.david.davidlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.util.listener.ListenerHandler;
import me.david.davidlib.util.listener.ValueChangeListener;

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
