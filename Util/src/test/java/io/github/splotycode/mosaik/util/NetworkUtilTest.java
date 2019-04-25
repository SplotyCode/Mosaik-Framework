package io.github.splotycode.mosaik.util;

import org.junit.jupiter.api.Test;

public class NetworkUtilTest {

    @Test
    void isLocal() {
        /*  */
        String[] localAddresses = new String[] {
                "localhost",
                "[::]",
                "[0:0:0:0:0:0:0:0]",
                "0.0.0.0",
                "127.0.0.2",
                "127.0.0.1",
                //"0177.1", DOES NOT WORK
                "0x7f.1",
                "127.1",
                "[::1]",
                "017700000001",
                "0x7f.1",
                "0x7f.0.0.1",
                "0x7f000001",
                "2130706433",
                "127.000.000.001",
                "127.0.1",
                "[0:0:0:0:0:ffff:0.0.0.0]",
                "[::ffff:0.0.0.0]",
                "[::ffff:0:0]",
                "[0:0:0:0:0:ffff:127.0.0.1]",
                "[::ffff:127.0.0.1]",
                "[::ffff:7f00:1]",
                "[0:0:0:0:0:ffff:127.0.0.2]",
                "[::ffff:127.0.0.2]",
                "[::ffff:7f00:2]"
        };
        for (String address : localAddresses) {
            if (!NetworkUtil.isLocalAddress(address)) {
                throw new AssertionError(address + " is not detected as local");
            }
        }
    }

}
