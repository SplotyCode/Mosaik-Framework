package io.github.splotycode.mosaik.networking.template;

import io.github.splotycode.mosaik.networking.template.packets.AddFilePacket;
import io.github.splotycode.mosaik.networking.template.packets.DestroyTemplatePacket;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;
import lombok.Data;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Data
public class Template {

    private String name;
    private File file;
    private TemplateService service;
    private HashMap<String, ResourceFile> files = new HashMap<>();

    public Template(String name, TemplateService service) {
        this.name = name;
        this.service = service;
        file = new File(service.getDirectory(), name);
    }

    public void addFile(String name, byte[] content) {
        if (!PathUtil.validAndNoUpwardTravel(file, name)) throw new IllegalArgumentException("Illegal template name");
        service.getMaster().sendAll(new AddFilePacket(this.name, name, System.currentTimeMillis(), service.getKit().selfAddress().asString(), content));
    }

    public void addFileLocal(ResourceFile file) {
        files.put(file.getName(), file);
    }

    public void addFileSynced(ResourceFile file) {
        addFileLocal(file);
        service.getIOQueue().execute(() -> {
            FileUtil.writeToFile(file.getFile(), file.getContent());

            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file.getInfoFile()))){
                dos.writeLong(file.getLastChanged());
                dos.writeUTF(file.getLastServer());
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        });
    }

    public void destroy() {
        service.getMaster().sendAll(new DestroyTemplatePacket(name));
    }
}
