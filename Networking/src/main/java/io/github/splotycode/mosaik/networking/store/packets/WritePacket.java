package io.github.splotycode.mosaik.networking.store.packets;

import lombok.Data;

@Data
public class WritePacket {

    private String key;
    private byte[] value;

}
