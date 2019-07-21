package io.github.splotycode.mosaik.networking.template;

import io.github.splotycode.mosaik.networking.template.packets.SyncPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SyncUtil {

    public static void handleSync(SyncPacket packet, TemplateService service) {
        for (ResourceFile file : packet.getFiles()) {
            ((SyncPacket.UnSyncFile) file).installLocal(service);
        }
    }

    public static SyncPacket createSyncPacket(HashMap<String, Long> lastSync, TemplateService service) {
        ArrayList<ResourceFile> resources = new ArrayList<>();
        for (Template template : service.getTemplates().values()) {
            for (ResourceFile resource : template.getFiles().values()) {
                if (resource.lastChanged > lastSync.getOrDefault(resource.lastServer, Long.MAX_VALUE)) {
                    resources.add(resource);
                }
            }
        }
        return new SyncPacket(resources);
    }

}
