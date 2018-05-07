package me.david.davidlib.netty;

import lombok.Getter;
import lombok.Setter;

public class NetSession {

    @Getter @Setter private long connected;
    @Getter @Setter private String iMac;

    public NetSession(String iMac) {
        this.iMac = iMac;
    }

}
