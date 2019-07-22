package io.github.splotycode.mosaik.networking.template.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.template.ResourceFile;
import io.github.splotycode.mosaik.networking.template.Template;
import io.github.splotycode.mosaik.networking.template.TemplateService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SyncPacket implements SerializedPacket {

    private List<ResourceFile> files;

    public class UnSyncFile extends ResourceFile {

        private String templateName;

        public UnSyncFile(String template, String name, byte[] content, long lastChanged, String lastServer) {
            super(template, name, content, lastChanged, lastServer);
            templateName = template;
        }

        public void installLocal(TemplateService service) {
            Template template = service.getTemplates().computeIfAbsent(templateName, s -> {
                Template temp = new Template(s, service);
                service.getIOQueue().execute(() -> temp.getFile().mkdir());
                return temp;
            });
            template.addFileSynced(new ResourceFile(template, name, content, lastChanged, lastServer));
        }
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        int size = packet.readVarInt();
        files = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String template = packet.readString();
            String name = packet.readString();
            byte[] content = packet.readArray();
            files.add(new UnSyncFile(template, name, content, packet.readLong(), packet.readString()));
        }
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(files.size());
        for (ResourceFile resource : files) {
            packet.writeString(resource.getTemplate().toString());
            packet.writeString(resource.getName());
            packet.writeArray(resource.getContent());
            packet.writeLong(resource.getLastChanged());
            packet.writeString(resource.getLastServer());
        }
    }
}
