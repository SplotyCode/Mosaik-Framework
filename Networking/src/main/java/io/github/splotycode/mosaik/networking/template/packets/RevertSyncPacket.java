package io.github.splotycode.mosaik.networking.template.packets;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class RevertSyncPacket extends StartSyncPacket {

    public RevertSyncPacket(HashMap<String, Long> newest) {
        super(newest);
    }
}
