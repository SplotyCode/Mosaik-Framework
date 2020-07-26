package io.github.splotycode.mosaik.runtime;

import io.github.splotycode.mosaik.runtime.application.IApplicationManager;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.runtime.startup.IStartUpManager;
import io.github.splotycode.mosaik.runtime.startup.StartUpConfiguration;
import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Runtime {
    @Getter protected static Runtime runtime;

    @Getter protected final BootContext bootContext;
    @Getter protected final StartUpConfiguration startUpConfiguration;
    @Getter protected ClassPath globalClassPath;

    public abstract IApplicationManager getApplicationManager();
    public abstract IStartUpManager getStartUpManager();
}
