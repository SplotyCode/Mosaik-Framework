package io.github.splotycode.mosaik.util.collection;

import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static io.github.splotycode.mosaik.util.collection.ArrayUtil.insert;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ArrayUtilTest {

    public static final int[] EXPECTED = ArrayUtil.collectInt(1, 2, 3, 4);
    public static final int[] UNFINISHED = ArrayUtil.collectInt(1, 3, 4);

    @Test
    public void insertAt() {
        assertArrayEquals(EXPECTED, insert(UNFINISHED,1, 2));
    }

    @Test
    public void missingMethod() {
        HashMap<String, Integer> counter = new HashMap<>();
        for (Method method : ReflectionUtil.getAllMethods(ArrayUtil.class)) {
            counter.put(method.getName(), counter.getOrDefault(method.getName(), 0) + 1);
        }
        for (Map.Entry<String, Integer> method : counter.entrySet()) {
            String name = method.getKey();
            boolean hasGeneric = !name.equals("toPrimitive") &&
                                 !name.equals("toObject") &&
                                 !name.equals("average") &&
                                 !name.equals("sum") &&
                                 !name.equals("max") &&
                                 !name.equals("min");
            int methods = hasGeneric ? 9 : 8;
            if (method.getValue() > 1 && method.getValue() % methods != 0) {
                throw new IllegalStateException("Missing method " + method.getKey() + " " + method.getValue() + "/" + (int) (Math.ceil(methods / method.getValue()) * methods));
            }
        }
    }

}
