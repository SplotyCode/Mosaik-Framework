package io.github.splotycode.mosaik.argparser;

import java.util.Map;

public interface IArgParser {

    void parseArgs(Object obj);
    void parseArgs(Object obj, String label);

    void parseArgs(Object obj, String[] args);
    void parseArgs(Object obj, String label, String[] args);

    Map<String, String> getParameters(String[] args);
    Map<String, String> getParameters(String label, String[] args);
    Map<String, String> getParameters();
    Map<String, String> getParameters(String label);

}
