package me.david.davidlib.info;

import me.david.davidlib.utils.StringUtil;
import me.david.davidlib.utils.VersionComparingUtil;
import me.david.davidlib.utils.reflection.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SystemInfo {

    public static final String OS_NAME = System.getProperty("os.name", "Unknown");
    public static final String OS_NAME_LOWER = OS_NAME.toLowerCase(Locale.ENGLISH);
    public static final String OS_VERSION = System.getProperty("os.version", "Unknown").toLowerCase(Locale.US);
    public static final OperationSystem OPERATION_SYSTEM = OperationSystem.current();

    public static final String OS_ARCH = System.getProperty("os.arch", "Unknown");

    public static final String JAVA_VERSION = System.getProperty("java.version", "Unknown");
    public static final String JAVA_RUNTIME_VERSION = System.getProperty("java.runtime.version", JAVA_VERSION);
    public static final String JAVA_VENDOR = System.getProperty("java.vendor", "Unknown");
    public static final String VM_VENDOR = System.getProperty("java.vm.vendor", JAVA_VENDOR);
    public static final String VM_VERSION = System.getProperty("java.vm.version");
    public static final String VM_NAME = System.getProperty("java.vm.name", "Unknown");

    public static final boolean isWindows = OS_NAME_LOWER.contains("windows");
    public static final boolean isMac = OS_NAME_LOWER.contains("mac");
    public static final boolean isLinux = OS_NAME_LOWER.contains("linux");
    public static final boolean isFreeBSD = OS_NAME_LOWER.contains("freebsd");
    public static final boolean isSolaris = OS_NAME_LOWER.contains("sunos");
    public static final boolean isUnix = !isWindows;

    public static final String ARCH_DATA_MODEL = System.getProperty("sun.arch.data.model", "unknown");
    public static final boolean IS_32_BIT = ARCH_DATA_MODEL == null || ARCH_DATA_MODEL.equals("32");
    public static final boolean IS_64_BIT = !IS_32_BIT;

    public static final boolean isOracleJvm = StringUtils.containsIgnoreCase(VM_VENDOR, "Oracle");

    public static final boolean IS_AT_LEAST_JAVA9 = ReflectionUtil.methodExsits(Class.class, "getModule");
    public static final boolean IS_AT_LEAST_JAVA8 = isJavaVersionAtLeast(1, 8, 0);
    public static final boolean IS_AT_LEAST_JAVA7 = isJavaVersionAtLeast(1, 7, 0);

    public static boolean isOsVersionAtLeast(String version) {
        return VersionComparingUtil.compare(OS_VERSION, version) >= 0;
    }

    public static final boolean isWin2kOrNewer = isWindows && isOsVersionAtLeast("5.0");
    public static final boolean isWinXpOrNewer = isWindows && isOsVersionAtLeast("5.1");
    public static final boolean isWinVistaOrNewer = isWindows && isOsVersionAtLeast("6.0");
    public static final boolean isWin7OrNewer = isWindows && isOsVersionAtLeast("6.1");
    public static final boolean isWin8OrNewer = isWindows && isOsVersionAtLeast("6.2");
    public static final boolean isWin10OrNewer = isWindows && isOsVersionAtLeast("10.0");

    public static final boolean isXWindow = isUnix && !isMac;
    public static final boolean isWayland = isXWindow && !StringUtil.isEmpty(System.getenv("WAYLAND_DISPLAY"));

    public static final boolean isGNOME = isXWindow && ReflectionUtil.clazzExists("org.GNOME.Accessibility.AtkValue");
    /* https://userbase.kde.org/KDE_System_Administration/Environment_Variables#KDE_FULL_SESSION */
    public static final boolean isKDE = isXWindow && !StringUtil.isEmpty(System.getenv("KDE_FULL_SESSION"));

    public static final boolean isMacOSTiger = isMac && isOsVersionAtLeast("10.4");
    public static final boolean isMacOSLeopard = isMac && isOsVersionAtLeast("10.5");
    public static final boolean isMacOSSnowLeopard = isMac && isOsVersionAtLeast("10.6");
    public static final boolean isMacOSLion = isMac && isOsVersionAtLeast("10.7");
    public static final boolean isMacOSMountainLion = isMac && isOsVersionAtLeast("10.8");
    public static final boolean isMacOSMavericks = isMac && isOsVersionAtLeast("10.9");
    public static final boolean isMacOSYosemite = isMac && isOsVersionAtLeast("10.10");
    public static final boolean isMacOSElCapitan = isMac && isOsVersionAtLeast("10.11");
    public static final boolean isMacOSSierra = isMac && isOsVersionAtLeast("10.12");
    public static final boolean isMacOSHighSierra = isMac && isOsVersionAtLeast("10.13");
    public static final boolean isMacOSMojave = isMac && isOsVersionAtLeast("10.14");

    public static String getMacOSMajorVersion(String version) {
        int[] parts = getMacOSVersionParts(OS_VERSION);
        return String.format("%d.%d", parts[0], parts[1]);
    }

    public static String getMacOSVersionCode() {
        int[] parts = getMacOSVersionParts(OS_VERSION);
        return String.format("%02d%d%d", parts[0], normalize(parts[1]), normalize(parts[2]));
    }

    public static String getMacOSMajorVersionCode() {
        int[] parts = getMacOSVersionParts(OS_VERSION);
        return String.format("%02d%d%d", parts[0], normalize(parts[1]), 0);
    }

    public static String getMacOSMinorVersionCode() {
        int[] parts = getMacOSVersionParts(OS_VERSION);
        return String.format("%02d%02d", parts[1], parts[2]);
    }

    private static int[] getMacOSVersionParts(String version) {
        List<String> parts = Arrays.asList(version.split("\\."));
        while (parts.size() < 3) {
            parts.add("0");
        }
        return new int[]{toInt(parts.get(0)), toInt(parts.get(1)), toInt(parts.get(2))};
    }

    public static String getOsNameAndVersion() {
        return OS_NAME + " " + OS_VERSION + " (Implementation: " + OPERATION_SYSTEM.name() + ")";
    }


    public static String getOsNameVersionAndArch() {
        return getOsNameAndVersion() + " " + OS_ARCH + " (" + ARCH_DATA_MODEL + ")";
    }

    private static int normalize(int number) {
        return number > 9 ? 9 : number;
    }

    private static int toInt(String string) {
        try {
            return Integer.valueOf(string);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean isJavaVersionAtLeast(int major, int minor, int update) {
        return JavaVersion.current().compareTo(new JavaVersion(major, minor, update, 0)) >= 0;
    }

}
