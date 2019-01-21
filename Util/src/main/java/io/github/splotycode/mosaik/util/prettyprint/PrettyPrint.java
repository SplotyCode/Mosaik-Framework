package io.github.splotycode.mosaik.util.prettyprint;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.*;

public class PrettyPrint {

    private Object object;
    private String prefix;

    public PrettyPrint(Object object, String prefix){
        this.object = object;
        this.prefix = prefix;
    }

    public String prettyPrint(){
        return prettyPrint(0);
    }

    private String prettyPrint(int tab){
        tab++;
        StringBuilder builder = new StringBuilder();
        builder.append(object.getClass().getSimpleName());
        builder.append("[\n").append(prefix);
        if (object.getClass().isAnnotationPresent(IgnorePrint.class)) builder.append("Print Disabled");
        else {
            for (Field field : getFields(object.getClass())) {
                for (int i = 0; i < tab; i++) builder.append("    ");
                field.setAccessible(true);
                //System.out.println(field.getName() + " " + object.getClass().getSimpleName());
                try {
                    builder.append(field.getName()).append(": ").append(getValue(field, tab)).append("\n").append(prefix);
                } catch (IllegalAccessException ex) {
                    builder.append("error");
                }
            }
        }
        for(int i = 0;i < tab-1;i++) builder.append("    ");
        builder.append(']');
        return builder.toString();
    }

    private String getValue(Field field, int tab) throws IllegalAccessException {
        field.setAccessible(true);
        Object o = field.get(object);
        if (o == null)
            return "null";
        //System.out.println(field.getName() + " : " + o.toString());
        Class<?> arrayType = o.getClass().getComponentType();
        if (arrayType != null) {
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
        } else if(field.getType().isEnum()){
            return field.getClass().getName().toUpperCase() + "." + o.toString() + "(enum)";
        }else if (o instanceof Set) {
            Set<Object> set = (Set<Object>) o;
            ArrayList<String> list = new ArrayList<>(set.size());
            for(Object obj: set) {
                list.add(obj.toString());
            }
            Collections.sort(list);
            return list.toString();
        } else if (o instanceof Map) {
            Map<Object,Object> map= (Map<Object, Object>) o;
            ArrayList<String> list= new ArrayList<>(map.size());
            for(Object key: map.keySet()) {
                list.add(key.toString()+"="+map.get(key));
            }
            Collections.sort(list);
            return list.toString();
        } else if (o instanceof Collection) {
            return o.toString();
        } else if (o instanceof Calendar) {
            DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
            Calendar c = (Calendar) o;
            return f.format(c.getTime()) + " milliSecond=" + c.get(Calendar.MILLISECOND);
        } else if(o instanceof String || o instanceof Number || o instanceof Boolean || field.getAnnotation(IgnorePrint.class) != null){
            return o.toString();
        }else {
            PrettyPrint obj = new PrettyPrint(o, prefix);
            return obj.prettyPrint(tab);
        }
    }

    private List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
