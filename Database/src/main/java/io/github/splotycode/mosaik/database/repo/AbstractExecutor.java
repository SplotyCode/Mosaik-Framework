package io.github.splotycode.mosaik.database.repo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.splotycode.mosaik.database.Database;
import io.github.splotycode.mosaik.database.DatabaseApplicationType;
import io.github.splotycode.mosaik.database.connection.ConnectionProvider;
import io.github.splotycode.mosaik.database.table.*;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformerNotFoundException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractExecutor<T, C extends ConnectionProvider> implements TableExecutor<T, C> {

    private GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
    private Gson gson;
    private C defaultConnection;
    private DatabaseApplicationType defaultApplication;

    {
        for (ValueTransformer<?, ?> transformer : TransformerManager.getInstance().getList()) {
            Class obj = String.class.isAssignableFrom(transformer.getInputClass())
                    ? transformer.getOutputClass() :
                    (String.class.isAssignableFrom(transformer.getOutputClass()) ? transformer.getInputClass() : null);
            if (obj != null) {
                builder.registerTypeAdapter(obj, new TypeAdapter() {
                    @Override
                    public void write(JsonWriter out, Object value) throws IOException {
                        out.value(doSave(value));
                    }

                    @Override
                    public Object read(JsonReader in) throws IOException {
                        return doParse(in.nextString(), obj);
                    }
                });
            }
        }
        gson = builder.create();
    }

    protected Class<?> clazz;
    protected Table table;
    protected String name;

    protected HashMap<String, FieldObject> fields = new HashMap<>();
    protected FieldObject primary = null;

    protected ColumnNameResolver[] allResolvers;

    private void resolveAllResolvers() {
        allResolvers = fields.values().stream()
                .map(obj -> (ColumnNameResolver) obj::getName)
                .toArray(ColumnNameResolver[]::new);
    }

    protected String getValue(String field, T instance) {
        return getValue(fields.get(field), instance);
    }

    protected String getValue(FieldObject fieldObject, T instance) {
        Field field = fieldObject.getField();
        field.setAccessible(true);
        try {
            return doSave(field.get(instance));
        } catch (IllegalAccessException e) {
            throw new RepoException("Could not getvalue for " + field.getName(), e);
        }
    }

    public void setValue(String fieldName, Object value, T instance) throws IllegalAccessException {
        Field field = fields.get(fieldName).getField();
        field.setAccessible(true);
        field.set(instance, doParse(value, field.getType()));
    }

    protected DataFactory getConfig() {
        DataFactory config = new DataFactory();
        config.putData(CommonData.AVOID_NULL, true);
        config.putData(CommonData.SERIALIZATION, true);
        return config;
    }

    protected String doSave(Object input) {
        DataFactory config = getConfig();
        config.putData(CommonData.AVOID_TOSTRING, true);

        try {
            return TransformerManager.getInstance().transform(config, input, String.class);
        } catch (TransformerNotFoundException ex) {
            return gson.toJson(input);
        }
    }

    protected <P> P doParse(Object input, Class<P> clazz) {
        try {
            return TransformerManager.getInstance().transform(getConfig(), input, clazz);
        } catch (TransformerNotFoundException ex) {
            return gson.fromJson(input.toString(), clazz);
        }
    }

    public AbstractExecutor(Class<?> clazz, DatabaseApplicationType defaultApplication) {
        this(clazz);
        this.defaultApplication = defaultApplication;
    }


    public AbstractExecutor(Class<?> clazz, C defaultConnection) {
        this(clazz);
        this.defaultConnection = defaultConnection;
    }

    public AbstractExecutor(Class<?> clazz) {
        this.clazz = clazz;
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new RepoException(clazz.getSimpleName() + " need Table anotation");
        }
        table = clazz.getAnnotation(Table.class);

        if (table.name().equals("")) {
            name = clazz.getSimpleName();
        } else {
            name = table.name();
        }

        for (Field field : ReflectionUtil.getAllFields(clazz)) {
            if (field.isAnnotationPresent(Column.class)) {
                FieldObject object = new FieldObject(field.getAnnotation(Column.class), field);
                if (fields.containsKey(object.getName())) {
                    throw new RepoException("Column " + object.getName() + " is defined twice");
                }
                fields.put(object.getName(), object);

                if (field.isAnnotationPresent(Primary.class)) {
                    if (primary == null) {
                        primary = object;
                    } else throw new RepoException("Can not have multiple Primary Annotations " + primary.getField().getName() + " and " + object.getField().getName());
                }
            } else if (field.isAnnotationPresent(Primary.class)) {
                throw new RepoException("Can not have @Primary without @Column for field " + field.getName());
            }
        }

        resolveAllResolvers();
    }

    @Override
    @SuppressWarnings("unchecked")
    public C getDefaultConnection() {
        if (defaultConnection != null) {
            return defaultConnection;
        }
        try {
            if (defaultApplication != null) {
                return (C) defaultApplication.getDefaultConnection();
            }
            return (C) Database.getInstance().getDefaultConnection();
        } catch (ClassCastException ex) {
            throw new RepoException("Default Connection has the wrong type", ex);
        }
    }

    @Override
    public Class<?> getRepoClass() {
        return clazz;
    }
}
