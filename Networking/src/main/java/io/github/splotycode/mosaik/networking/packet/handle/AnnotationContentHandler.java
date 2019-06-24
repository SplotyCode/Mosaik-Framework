package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.collection.MultiHashMap;
import io.github.splotycode.mosaik.util.reflection.classregister.ClassRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class AnnotationContentHandler<P extends Packet> extends SimpleChannelInboundHandler<P> implements ClassRegister<Object> {

    public AnnotationContentHandler(Class<? extends P> clazz, Object obj) {
        super(clazz);
        register(obj);
    }

    @AllArgsConstructor
    private static class HandlerData {

        private Method method;
        private Object object;

        public String displayName() {
            return method.getDeclaringClass().getName() + "#" + method.getName();
        }

    }

    @Override
    public void register(Object obj) {
        Class clazz = obj.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(PacketTarget.class)) {
                if (method.getParameterCount() != 1 &&
                        !(method.getParameterCount() == 2 && method.getParameterTypes()[1] == ChannelHandlerContext.class)) {
                    throw new AnnotationStructureExcpetion("Invalid Parameter length on " + clazz.getName() + "#" + method.getName());
                }
                Class<?> parameter = method.getParameterTypes()[0];
                if (Packet.class.isAssignableFrom(parameter)) {
                    handlers.addToList((Class<? extends Packet>) parameter, new HandlerData(method, obj));
                } else {
                    throw new AnnotationStructureExcpetion("Parameter need to extend Packet");
                }
            }
        }
    }

    @Override
    public void unRegister(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Object> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<Object> getObjectClass() {
        return Object.class;
    }

    private static MultiHashMap<Class<? extends Packet>, HandlerData> handlers = new MultiHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, P p) throws Exception {
        for (HandlerData handlerData : handlers.get(p.getClass())) {
            try {
                if (handlerData.method.getParameterCount() == 1) {
                    handlerData.method.invoke(handlerData.object, p);
                } else {
                    handlerData.method.invoke(handlerData.object, p, ctx);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PacketHandleExcpetion("Failed to execute handler Method: " + handlerData.displayName(), e);
            }
        }
    }
}
