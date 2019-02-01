package io.github.splotycode.mosaik.argparserimpl;

import io.github.splotycode.mosaik.argparser.IArgParser;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Main Arg Parser
 */
public class ArgParser implements IArgParser {

    private Map<String[], ParsedArguments> cachedArguments = new HashMap<>();
    private Map<Object, ParsedObject> cachedObjects = new HashMap<>();

    /**
     * Parses arguments to an object wit a prefix
     * @param obj the object you want to apply the arguments
     * @param label the prefix
     * @param args the arguments you want to parse
     */
    @Override
    public void parseArgs(Object obj, String label, String[] args) {
        ParsedArguments arguments = cachedArguments.get(args);
        ParsedObject object = cachedObjects.get(obj);

        if (arguments == null) {
            arguments = ParsedArguments.parse(args);
            cachedArguments.put(args, arguments);
        }

        if (object == null) {
            object = ParsedObject.parse(obj);
            cachedObjects.put(obj, object);
        }

        for (Argument argument : object.getAll()) {
            String name = label == null ? "" : label + ":" + argument.getName();
            String rawValue = arguments.getByKey(name);
            if (rawValue == null) {
                if (argument.getParameter().needed()) {
                    throw new ArgParseException("Could not fill argument " + name + " because it foes not exsits in arg");
                }
                continue;
            }
            Object result = TransformerManager.getInstance().transform(rawValue, argument.getField().getType());
            try {
                argument.getField().set(obj, result);
            } catch (IllegalAccessException e) {
                throw new ArgParseException("Could not access " + obj.getClass().getName() + "#" + argument.getField().getName(), e);
            }
        }
    }

    /**
     * Parses the Boot Parameters to an Object
     * @param obj the object you want to apply the boot parameters
     */
    @Override
    public void parseArgs(Object obj) {
        parseArgs(obj, LinkBase.getBootContext().getArgs());
    }

    /**
     * Parses the Boot Parameters to an Object with a prefix
     * @param obj the object you want to apply the boot parameters
     * @param label the prefix
     */
    @Override
    public void parseArgs(Object obj, String label) {
        parseArgs(obj, label, LinkBase.getBootContext().getArgs());
    }

    /**
     * Parses arguments to an object
     * @param obj the object you want to apply the arguments
     * @param args the arguments you want to parse
     */
    @Override
    public void parseArgs(Object obj, String[] args) {
        parseArgs(obj, null, args);
    }

}
