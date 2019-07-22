package io.github.splotycode.mosaik.automatisation.generators;

import java.util.concurrent.ThreadLocalRandom;

public class UserAgendGenerator implements Generator<String> {

    private SystemInfoGenerator systemGen = new SystemInfoGenerator();
    private String[] CHROME_VERSIONS = new String[] {"74.0.3729.169", "58.0.3029.110", "51.0.2704.79"};
    private String[] WEBKIT_VERSION = new String[] {"533.19.4", "537.36"};

    @Override
    public String getRandom() {
        SystemInfoGenerator.SystemInfo info = null;
        while (info == null || info.getPlatform().equals("Macintosh")) {
            //TODO safari support?
            info = systemGen.getRandom();
        }

        boolean firefox = fastRandom(1, 3) == 2;
        StringBuilder agend = new StringBuilder("Mozilla/5.0 (");
        platformString(agend, info);
        if (firefox) {
            int version = fastRandom(53, 68);
            agend.append("rv:").append(version).append(".0) Gecko/20100101 Firefox/").append(version).append(".0");
        } else {
            agend.setLength(agend.length() - 2);
            agend.append(") ");
            String webkit = pick(WEBKIT_VERSION);
            agend.append("AppleWebKit/").append(webkit)
                    .append(" (KHTML, like Gecko) Chrome/").append(pick(CHROME_VERSIONS))
                    .append(" Safari/").append(webkit);
        }
        return agend.toString();
    }

    private void platformString(StringBuilder agend, SystemInfoGenerator.SystemInfo info) {
        agend.append(info.getPlatform()).append("; ");
        if (info.getDistro() != null) {
            agend.append(info.getDistro()).append("; ");
            agend.append(info.getKernel()).append(" ").append(info.isBit64() ? "x64" : "x32").append("; ");
        } else {
            agend.append(info.getKernel()).append("; ");
            agend.append(info.isBit64() ? "x64" : "x32").append("; ");
        }
    }

    private String pick(String[] array) {
        return array[fastRandom(0, array.length - 1)];
    }

    private int fastRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
