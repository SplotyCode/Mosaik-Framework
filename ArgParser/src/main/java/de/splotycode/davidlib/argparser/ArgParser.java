package de.splotycode.davidlib.argparser;

import de.splotycode.davidlib.argparser.parser.ArgParseException;
import de.splotycode.davidlib.argparser.parser.Argument;
import de.splotycode.davidlib.argparser.parser.ParsedArguments;
import de.splotycode.davidlib.argparser.parser.ParsedObject;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.argparser.IArgParser;

import java.util.HashMap;
import java.util.Map;

public class ArgParser implements IArgParser {

    private Map<String[], ParsedArguments> cachedArguments = new HashMap<>();
    private Map<Object, ParsedObject> cachedObjects = new HashMap<>();

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
            if (rawValue == null) throw new ArgParseException("Could not fill argument " + name + " because it foes not exsits in arg");
            Object result = LinkBase.getTransformerManager().transform(rawValue, argument.getField().getType());
            try {
                argument.getField().set(obj, result);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void parseArgs(Object obj) {
        parseArgs(obj, LinkBase.getBootContext().getArgs());
    }

    @Override
    public void parseArgs(Object obj, String label) {
        parseArgs(obj, label, LinkBase.getBootContext().getArgs());
    }

    @Override
    public void parseArgs(Object obj, String[] args) {
        parseArgs(obj, null, args);
    }

}
