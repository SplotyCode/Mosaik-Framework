package me.david.davidlib.runtime.parsing.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.davidlib.runtime.parsing.DomSourceType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@AllArgsConstructor
@Getter
public class DomStringInput implements DomInput {

    protected String input;

    @Override
    public byte[] getBytes() {
        return input.getBytes();
    }

    @Override
    public String getString() {
        return input;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(getBytes());
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.STRING;
    }
}
