package io.github.splotycode.mosaik.gameengine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BufferUtil {

    public static IntBuffer createBuffer(int[] data, boolean flip){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        if (flip) buffer.flip();
        return buffer;
    }

    public static FloatBuffer createBuffer(float[] data, boolean flip){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        if (flip) buffer.flip();
        return buffer;
    }

    public static DoubleBuffer createBuffer(double[] data, boolean flip){
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(data.length);
        buffer.put(data);
        if (flip) buffer.flip();
        return buffer;
    }

}
