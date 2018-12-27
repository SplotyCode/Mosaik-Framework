package me.david.davidlib.info;

import me.david.davidlib.utils.StringUtil;

import java.lang.management.ManagementFactory;
import java.util.List;

import static me.david.davidlib.info.SystemInfo.*;

public final class EnvironmentInformation {

    public static String getJDKInfo() {
        return JAVA_VERSION + " from " + JAVA_VENDOR;
    }

    public static String getJREInfo() {
        return JAVA_RUNTIME_VERSION;
    }

    public static String getJVMInfo() {
        return VM_NAME + " version " + VM_VERSION + " from " + VM_VENDOR;
    }

    public static String getJVMArgs() {
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        if (arguments != null) {
            return StringUtil.join(arguments, " ");
        }
        return "Unknown or not present";
    }

}
