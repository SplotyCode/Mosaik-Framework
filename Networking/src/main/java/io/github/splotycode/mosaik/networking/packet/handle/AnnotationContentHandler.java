package io.github.splotycode.mosaik.networking.packet.handle;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.reflection.classregister.ClassRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class AnnotationContentHandler<P extends Packet> extends SimpleChannelInboundHandler<P> implements ClassRegister<Object> {

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
                if (method.getParameterCount() == 1) {
                    throw new AnnotationStructureExcpetion("Invalid Parameter length");
                }
                Class<?> parameter = method.getParameterTypes()[0];
                if (Packet.class.isAssignableFrom(parameter)) {
                    handlers.put((Class<? extends Packet>) parameter, new HandlerData(method, obj));
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

    private static Multimap<Class<? extends Packet>, HandlerData> handlers = new HashMultimap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, P p) throws Exception {
        for (HandlerData handlerData : handlers.get(p.getClass())) {
            try {
                handlerData.method.invoke(handlerData.object, p);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PacketHandleExcpetion("Failed to execute handler Method: " + handlerData.displayName(), e);
            }
        }
    }
}
