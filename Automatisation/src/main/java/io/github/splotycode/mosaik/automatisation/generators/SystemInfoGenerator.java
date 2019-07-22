package io.github.splotycode.mosaik.automatisation.generators;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

public class SystemInfoGenerator implements Generator<SystemInfoGenerator.SystemInfo> {

    private static String[] WINDOWS_PLATFORMS = new String[] {"Windows NT 10.0", "Windows NT 6.1"};
    private static String[] LINUX_DISTRO = new String[] {"Ubuntu"};

    @Override
    public SystemInfo getRandom() {
        int kernel = fastRandom(1, 5);
        boolean bit64 = fastRandom(3, 5) != 4;
        if (kernel == 5) {
            return new SystemInfo("X11", bit64, "Linux", pick(LINUX_DISTRO));
        } else if (kernel < 3) {
            return new SystemInfo("Macintosh", bit64, "Intel Mac OS " + getOSXVersion(), null);
        } else {
            return new SystemInfo(pick(WINDOWS_PLATFORMS), bit64, bit64 ? "Win64" : "Win32", null);
        }
    }

    private String getOSXVersion() {
        return fastRandom(8, 12) + "." + fastRandom(5, 8);
    }

    private String pick(String[] array) {
        return array[fastRandom(0, array.length - 1)];
    }

    private int fastRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    @AllArgsConstructor
    @Data
    public static class SystemInfo {

        private String platform;
        private boolean bit64;
        private String kernel;
        private String distro;

    }

}
