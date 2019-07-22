package io.github.splotycode.mosaik.networking.template;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.master.MasterChangeListener;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.template.packets.CreateTemplatePacket;
import io.github.splotycode.mosaik.networking.template.packets.SyncPacket;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.runtime.pathmanager.PathManager;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.concurrent.PausableExecutorService;
import io.github.splotycode.mosaik.util.io.PathUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class TemplateService implements Service, MasterChangeListener {

    private HashMap<String, Template> templates = new HashMap<>();
    private TemplateHandler handler = new TemplateHandler(this);
    private MasterTemplateHandler masterHandler = new MasterTemplateHandler(this);
    private PausableExecutorService iOQueue = new PausableExecutorService(Executors.newSingleThreadExecutor());

    private MasterService master;
    private CloudKit kit;

    private File directory;

    @Setter private boolean loaded;

    public TemplateService(CloudKit kit) {
        this.kit = kit;
    }

    @Override
    public void start() {
        master = kit.getServiceByClass(MasterService.class);
        master.getMasterRegistry().registerPackage(SyncPacket.class);
        master.addClientHandler(handler);
        master.addServerHandler(masterHandler);
        directory = new File(PathManager.getInstance().getMainDirectory(), "Templates");
        directory.mkdir();

        for (String file : directory.list()) {
            Template template = new Template(file, this);
            try {
                Files.walkFileTree(template.getFile().toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        template.addFileLocal(new ResourceFile(template, file.toFile()));
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
    }

    @Override
    public void stop() {
        loaded = false;
        iOQueue.shutdown();
        try {
            iOQueue.awaitTermination(2, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    @Override
    public ServiceStatus getStatus() {
        return master.getStatus();
    }

    @Override
    public String statusMessage() {
        return master.getStatus() != ServiceStatus.RUNNING ? "Waiting for Master" : loaded ? "Holding " + templates.size() : "Loading resources to memory";
    }

    public void create(String name) {
        if (!PathUtil.validAndNoUpwardTravel(directory, name)) throw new IllegalArgumentException("Illegal template name");
        master.sendAll(new CreateTemplatePacket(name));
    }

    public long newest() {
        long newest = Long.MIN_VALUE;
        for (Template template : templates.values()) {
            for (ResourceFile resource : template.getFiles().values()) {
                newest = Math.max(newest, resource.getLastChanged());
            }
        }
        return newest;
    }

    public HashMap<String, Long> newestMap() {
        HashMap<String, Long> map = new HashMap<>();
        for (Template template : templates.values()) {
            for (ResourceFile resource : template.getFiles().values()) {
                map.put(resource.lastServer, Math.max(resource.lastChanged, map.getOrDefault(resource.lastServer, Long.MIN_VALUE)));
            }
        }
        return map;
    }

    @Override
    public void onChange(boolean own, MosaikAddress current, MosaikAddress last) {
        loaded = own;
    }
}
