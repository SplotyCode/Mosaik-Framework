package me.david.davidlib.runtime.parsing.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.davidlib.runtime.parsing.DomSourceType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@AllArgsConstructor
@Getter
public class DomByteInput implements DomInput {

    protected byte[] input;

    @Override
    public byte[] getBytes() {
        return input;
    }

    @Override
    public String getString() {
        return new String(input);
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(input);
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.BYTES;
    }

}
