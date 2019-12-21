package io.github.splotycode.mosaik.util.info;

import io.github.splotycode.mosaik.util.io.LineSeparator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum OperationSystem {

    WINDOWS(LineSeparator.CRLF) {
        @Override
        protected String generatePingString(String remote, long timeout) {
            return "ping -n 1 -w " + timeout + " " + remote;
        }
    },
    LINUX(LineSeparator.LF) {
        @Override
        protected String generatePingString(String remote, long timeout) {
            return "ping -c 1 -W " + timeout / 1000 + " " + remote;
        }
    };

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

    protected abstract String generatePingString(String remote, long timeout);

    public static boolean isHostOnline(String remote, long timeout) {
        return current().pingHost(remote, timeout);
    }

    public boolean pingHost(String remote, long timeout) {
        try {
            Process process = Runtime.getRuntime().exec(generatePingString(remote, timeout));
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

}
