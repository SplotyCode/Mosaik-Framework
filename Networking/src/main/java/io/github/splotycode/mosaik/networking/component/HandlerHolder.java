package io.github.splotycode.mosaik.networking.component;

import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.channel.ChannelHandler;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.function.Supplier;

public class HandlerHolder {

    private static final Logger LOGGER = Logger.getInstance(HandlerHolder.class);

    @EqualsAndHashCode(callSuper = true)
    @Getter
    private class SharableHandlerData extends AbstractHandlerData {

        private final ChannelHandler handler;

        public SharableHandlerData(int priority, Class<? extends ChannelHandler> clazz, String name, ChannelHandler handler) {
            super(priority, name, clazz);
            this.handler = handler;
        }

        @Override
        ChannelHandler handler() {
            return handler;
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    private class HandlerData extends AbstractHandlerData {

        private final Supplier<ChannelHandler> handler;

        public HandlerData(int priority, Class<? extends ChannelHandler> clazz, String name, Supplier<ChannelHandler> handler) {
            super(priority, name, clazz);
            this.handler = handler;
        }

        @Override
        ChannelHandler handler() {
            return handler.get();
        }
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    public abstract class AbstractHandlerData {

        private final int priority;
        @Getter private final String name;
        private final Class<? extends ChannelHandler> clazz;

        abstract ChannelHandler handler();

    }

    private TreeMap<Integer, AbstractHandlerData> sorted = new TreeMap<>();
    private HashMap<Class<? extends ChannelHandler>, AbstractHandlerData> map = new HashMap<>();

    public Collection<AbstractHandlerData> getHandlerData() {
        return sorted.values();
    }


    public Iterator<ChannelHandler> getHandlers() {
        Iterator<AbstractHandlerData> iterator = sorted.values().iterator();
        return new Iterator<ChannelHandler>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ChannelHandler next() {
                return iterator.next().handler();
            }
        };
    }

    public void addHandler(int priority, String name, ChannelHandler obj) {
        addHandler(new SharableHandlerData(priority, obj.getClass(), name, obj));
    }

    public void addHandler(int priority, String name, Class<ChannelHandler> clazz, Supplier<ChannelHandler> obj) {
        addHandler(new HandlerData(priority, clazz, name, obj));
    }

    public void addHandler(AbstractHandlerData data) {
        AbstractHandlerData old = map.put(data.clazz, data);
        AbstractHandlerData oldSorted = sorted.put(data.priority, data);
        if (!Objects.equals(old, oldSorted)) {
            LOGGER.error("Tried to override handler by class but the priority is different");
        } else {
            if (old != null) {
                LOGGER.warn("Already registered handler with class" + data.clazz.getName());
            }
            if (old != null) {
                LOGGER.warn("Already registered handler with priority" + data.priority);
            }
        }
    }

    public <H extends ChannelHandler> H getHandler(Class<H> clazz) {
        AbstractHandlerData data = getHandlerData(clazz);
        return data == null ? null : (H) data.handler();
    }

    public String getHandlerName(Class<? extends ChannelHandler> clazz) {
        AbstractHandlerData data = getHandlerData(clazz);
        return data == null ? null : data.name;
    }

    public AbstractHandlerData getHandlerData(Class<? extends ChannelHandler> clazz) {
        return map.get(clazz);
    }

}
