package io.github.splotycode.mosaik.util.prettyprint;

import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.*;

public class PrettyPrint {

    public static void prettyPrint(Object object) {
        prettyPrint(System.out, object);
    }

    private static void prettyPrint(PrintStream printStream, Object object) {
        printStream.println(new PrettyPrint(object).prettyPrint());
    }

    public static void prettyPrintType(Object object) {
        prettyPrintType(System.out, object);
    }

    private static void prettyPrintType(PrintStream printStream, Object object) {
        printStream.println(new PrettyPrint(object).prettyPrintType());
    }

    private Object object;
    private String prefix;
    private int maxRecursion;
    private int startStackDepth;

    public PrettyPrint(Object object) {
        this(object, "");
    }

    public PrettyPrint(Object object, String prefix) {
        this(object, prefix, 120);
    }

    public PrettyPrint(Object object, String prefix, int maxRecursion) {
        this.object = object;
        this.prefix = prefix;
        this.maxRecursion = maxRecursion;
    }

    public String prettyPrint() {
        start();
        return prettyPrint(0);
    }

    public String prettyPrintType() {
        start();
        return getValue(object, 0);
    }

    private void start() {
        startStackDepth = Thread.currentThread().getStackTrace().length - 1;
    }

    private String prettyPrint(int tab) {
        tab++;
        StringBuilder builder = new StringBuilder();
        builder.append(object.getClass().getSimpleName());
        builder.append("[\n").append(prefix);
        if (object.getClass().isAnnotationPresent(IgnorePrint.class)) {
            builder.append("Print Disabled");
        } else {
            for (Field field : ReflectionUtil.getAllFields(object.getClass())) {
                appendIntent(builder, tab);
                field.setAccessible(true);
                try {
                    builder.append(field.getName()).append(": ")
                            .append(getValue(field, tab))
                            .append("\n").append(prefix);
                } catch (Exception ex) {
                    builder.append("error");
                }
            }
        }
        appendIntent(builder, tab - 1);
        builder.append(']');
        return builder.toString();
    }

    private void appendIntent(StringBuilder builder, int tabs) {
        for (int i = 0; i < tabs; i++) {
            builder.append("    ");
        }
    }

    private String getValue(Field field, int tab) throws IllegalAccessException {
        if (field.getAnnotation(IgnorePrint.class) != null) {
            return "Disabled";
        }
        field.setAccessible(true);
        return getValue(field.get(object), tab);
    }

    private String getValue(Object o, int tab) {
        if (o == null) {
            return "null";
        }
        if (Thread.currentThread().getStackTrace().length - startStackDepth > maxRecursion) {
            return "[infinitive recursion]";
        }
        Class<?> arrayType = o.getClass().getComponentType();
        if (arrayType != null && o.getClass().isArray()) {
            if (!arrayType.isPrimitive()) {
                return Arrays.deepToString((Object[]) o);
            } else if (arrayType.equals(Integer.TYPE)) {
                return Arrays.toString((int[]) o);
            } else if (arrayType.equals(Double.TYPE)) {
                return Arrays.toString((double[]) o);
            } else if (arrayType.equals(Boolean.TYPE)) {
                return Arrays.toString((boolean[]) o);
            } else if (arrayType.equals(Short.TYPE)) {
                return Arrays.toString((short[]) o);
            } else if (arrayType.equals(Long.TYPE)) {
                return Arrays.toString((long[]) o);
            } else if (arrayType.equals(Float.TYPE)) {
                return Arrays.toString((float[]) o);
            } else if (arrayType.equals(Character.TYPE)) {
                return Arrays.toString((char[]) o);
            } else if (arrayType.equals(Byte.TYPE)) {
                return Arrays.toString((byte[]) o);
            } else {
                return "?????????";
            }
        } else if(o.getClass().isEnum()){
            return o.getClass().getName().toUpperCase() + "." + o.toString() + "(enum)";
        } else if(o instanceof File) {
            return ((File) o).getAbsolutePath();
        } else if (o instanceof Collection) {
            Collection<Object> collection = (Collection<Object>) o;
            ArrayList<String> list = new ArrayList<>(collection.size());
            for(Object obj: collection) {
                list.add(getValue(obj, 0));
            }
            Collections.sort(list);
            return list.toString();
        } else if (o instanceof Map) {
            Map<Object,Object> map = (Map<Object, Object>) o;
            ArrayList<String> list = new ArrayList<>(map.size());
            for (Object key : map.keySet()) {
                list.add(getValue(key, 0) + "=" + getValue(map.get(key), 0));
            }
            Collections.sort(list);
            return list.toString();
        } else if (o instanceof Calendar) {
            DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
            Calendar c = (Calendar) o;
            return f.format(c.getTime()) + " milliSecond=" + c.get(Calendar.MILLISECOND);
        } else if (o instanceof String || o instanceof Number || o instanceof Boolean || o instanceof Character) {
            return o.toString();
        } else {
            PrettyPrint obj = new PrettyPrint(o, prefix);
            return obj.prettyPrint(tab);
        }
    }

}
