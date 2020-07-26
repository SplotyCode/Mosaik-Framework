package io.github.splotycode.mosaik.runtime.startup;

import io.github.splotycode.mosaik.runtime.logging.MosaikLoggerFactory;
import io.github.splotycode.mosaik.runtime.startup.condition.ConditionFailAction;
import io.github.splotycode.mosaik.runtime.startup.condition.StartupCondition;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

@Getter
@EqualsAndHashCode
public class StartUpConfiguration {

    private ClassLoaderProvider classLoader;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Class<? extends LoggerFactory>> bootLoggerFactory = Optional.empty();
    private Class<? extends LoggerFactory> runtimeLoggerFactory;

    private String[] args;
    private ArrayList<String> argList;

    private String linkBasePath;
    private Map<Class<? extends StartupCondition>, ConditionFailAction> conditionActions = new HashMap<>();

    public StartUpConfiguration withArgs(String[] args) {
        this.args = args;
        return this;
    }

    public StartUpConfiguration addArgument(String argument) {
        if (argList == null) {
            argList = new ArrayList<>();
        }
        argList.add(argument);
        return this;
    }

    public StartUpConfiguration withBootLoggerFactory(Class<? extends LoggerFactory> bootLoggerFactory) {
        this.bootLoggerFactory = Optional.of(bootLoggerFactory);
        return this;
    }

    public StartUpConfiguration withRuntimeLoggerFactory(Class<? extends LoggerFactory> runtimeLoggerFactory) {
        this.runtimeLoggerFactory = runtimeLoggerFactory;
        return this;
    }

    public StartUpConfiguration withClassLoader(ClassLoaderProvider classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public StartUpConfiguration withLinkBasePath(String linkBasePath) {
        this.linkBasePath = linkBasePath;
        return this;
    }

    public StartUpConfiguration withConditionFailAction(Class<? extends StartupCondition> clazz, ConditionFailAction action) {
        conditionActions.put(clazz, action);
        return this;
    }

    public void finish() {
        if (argList != null) {
            if (args != null) {
                argList.addAll(Arrays.asList(args));
            }
            args = argList.toArray(new String[0]);
        } else if (args == null) {
            args = ArrayUtil.EMPTY_STRING_ARRAY;
        }
        if (runtimeLoggerFactory == null) {
            runtimeLoggerFactory = MosaikLoggerFactory.class;
        }
        if (classLoader == null) {
            classLoader = ClassLoaderProvider.DefaultClassLoaderProvider.INSTANCE;
        }
        if (linkBasePath == null) {
            linkBasePath = "linkbase.txt";
        }
    }

    public BootContext getBootContext(Instant startTime) {
        return new BootContext(args, startTime, classLoader);
    }
}
