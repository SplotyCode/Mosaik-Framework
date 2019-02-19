package io.github.splotycode.mosaik.runtime.debug;

public enum DebugModes implements DebugModeType {

    ALL,
    LOG,
    LOG_FILE;

    @Override
    public String modeName() {
        return name();
    }
}
