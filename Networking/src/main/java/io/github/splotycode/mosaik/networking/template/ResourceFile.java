package io.github.splotycode.mosaik.networking.template;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.io.BinaryUtil;
import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.Data;

import java.io.*;

@Data
public class ResourceFile {

    private Template template;
    protected String name;
    private File file;
    protected byte[] content;
    protected long lastChanged;
    protected String lastServer;
    private File infoFile;

    public ResourceFile(Template template, String name, byte[] content, long lastChanged, String lastServer) {
        this(template.getName(), name, content, lastChanged, lastServer);
        this.template = template;
    }

    public ResourceFile(String template, String name, byte[] content, long lastChanged, String lastServer) {
        this.name = name;
        this.content = content;
        file = new File(template, name);
        this.lastChanged = lastChanged;
        this.lastServer = lastServer;
        infoFile = new File(template, name + ".info");
    }

    public ResourceFile(Template template, File file) {
        this.template = template;
        this.file = file;
        name = file.getName();
        content = FileUtil.loadFileBytes(file);

        infoFile = new File(template.getFile(), name + ".info");
        try (DataInputStream dis = new DataInputStream(new FileInputStream(infoFile))){
            lastChanged = dis.readLong();
            lastServer = dis.readUTF();
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }
}
