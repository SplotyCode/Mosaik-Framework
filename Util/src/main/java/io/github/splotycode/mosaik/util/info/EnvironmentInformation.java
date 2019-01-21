package io.github.splotycode.mosaik.util.info;

import io.github.splotycode.mosaik.util.StringUtil;

import java.lang.management.ManagementFactory;
import java.util.List;

public final class EnvironmentInformation {

    public static String getJDKInfo() {
        return SystemInfo.JAVA_VERSION + " from " + SystemInfo.JAVA_VENDOR;
    }

    public static String getJREInfo() {
        return SystemInfo.JAVA_RUNTIME_VERSION;
    }

    public static String getJVMInfo() {
        return SystemInfo.VM_NAME + " version " + SystemInfo.VM_VERSION + " from " + SystemInfo.VM_VENDOR;
    }

    public static String getJVMArgs() {
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        if (arguments != null) {
            return StringUtil.join(arguments, " ");
        }
        return "Unknown or not present";
    }

}
