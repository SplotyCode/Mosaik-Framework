package me.david.davidlib.util.info;

public enum OperationSystem {

    WINDOWS,
    LINUX;

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

}
