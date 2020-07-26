package io.github.splotycode.mosaik.runtime;

import io.github.splotycode.mosaik.runtime.application.IApplicationManager;
import io.github.splotycode.mosaik.util.datafactory.DataFactoryComponent;
import io.github.splotycode.mosaik.util.io.IOUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkBase implements DataFactoryComponent {

    @Getter private static LinkBase instance = new LinkBase();

    @Getter private DataFactory linkFactory = new DataFactory();

    public <T> void registerLink(DataKey<T> key, T value) {
        linkFactory.putData(key, value);
    }

    public <T> T getLink(DataKey<T> key) {
        return linkFactory.getData(key);
    }

    public <T> T getLinkDefault(DataKey<T> key, T def) {
        return linkFactory.getDataDefault(key, def);
    }

    public static BootContext getBootContext() {
        return instance.getLink(Links.BOOT_DATA);
    }

    public static IApplicationManager getApplicationManager() {
        return instance.getLink(Links.APPLICATION_MANAGER);
    }

    public void loadLinkBaseFile(String path) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] lines = IOUtil.resourceToText(path).split("\n");
        for (String line : lines) {
            if (!line.startsWith("#")) {
                String[] lineSplit = line.split(": ");
                LinkBase.getInstance().getLinkFactory().putData(lineSplit[0], null, Class.forName(lineSplit[1]).newInstance());
            }
        }
    }

    @Override
    public DataFactory getDataFactory() {
        return linkFactory;
    }
}
