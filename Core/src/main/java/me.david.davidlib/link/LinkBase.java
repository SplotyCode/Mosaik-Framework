package me.david.davidlib.link;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.datafactory.DataFactory;
import me.david.davidlib.datafactory.DataKey;
import me.david.davidlib.startup.BootContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkBase {

    @Getter private static LinkBase instance = new LinkBase();

    @Getter private DataFactory linkFactory = new DataFactory();

    public <T> void registerLink(DataKey<T> key, T value) {
        linkFactory.putData(key, value);
    }

    public <T> T getLink(DataKey<T> key) {
        return linkFactory.getData(key);
    }

    public static BootContext getBootContext() {
        return instance.getLink(Links.BOOT_DATA);
    }

    public static IApplicationManager getApplicationManager() {
        return instance.getLink(Links.APPLICATION_MANAGER);
    }

}
