package me.david.davidlib.netty;

import lombok.Getter;
import lombok.Setter;

public class NetSession {

    @Getter @Setter protected long connected;
    @Getter @Setter protected String iMac;

    public NetSession(String iMac) {
        this.iMac = iMac;
    }

}
