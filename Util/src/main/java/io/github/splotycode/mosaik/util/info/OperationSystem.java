package io.github.splotycode.mosaik.util.info;

import io.github.splotycode.mosaik.util.io.LineSeparator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationSystem {

    WINDOWS(LineSeparator.CRLF),
    LINUX(LineSeparator.LF);

    private static OperationSystem current = null;

    private static OperationSystem current0() {
        if (SystemInfo.OS_NAME_LOWER.contains("win")) return WINDOWS;
        return LINUX;
    }

    public static OperationSystem current() {
        if (current == null) {
            current = current0();
        }
        return current;
    }

    private LineSeparator separator;

}
