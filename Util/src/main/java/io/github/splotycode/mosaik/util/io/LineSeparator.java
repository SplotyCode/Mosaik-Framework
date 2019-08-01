package io.github.splotycode.mosaik.util.io;

import io.github.splotycode.mosaik.util.info.OperationSystem;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public enum LineSeparator {

    LF("\n"),
    CRLF("\r\n"),
    CR("\r");

    public static LineSeparator getDefault() {
        return OperationSystem.current().getSeparator();
    }

    LineSeparator(String separator) {
        this.separator = separator;
    }

    private String separator;
    private byte[] bytes;

    public byte[] getBytes() {
        if (bytes == null) {
            bytes = separator.getBytes(StandardCharsets.UTF_8);
        }
        return bytes;
    }

}
