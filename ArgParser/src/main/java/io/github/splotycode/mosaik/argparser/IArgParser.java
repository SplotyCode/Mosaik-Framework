package io.github.splotycode.mosaik.argparser;

import java.util.Map;

/**
 * Main Arg Parser Interface
 */
public interface IArgParser {

    /**
     * Parses the Boot Parameters to an Object
     * @param obj the object you want to apply the boot parameters
     */
    void parseArgs(Object obj);
    /**
     * Parses the Boot Parameters to an Object with a prefix
     * @param obj the object you want to apply the boot parameters
     * @param label the prefix
     */
    void parseArgs(Object obj, String label);

    /**
     * Parses arguments to an object
     * @param obj the object you want to apply the arguments
     * @param args the arguments you want to parse
     */
    void parseArgs(Object obj, String[] args);
    /**
     * Parses arguments to an object wit a prefix
     * @param obj the object you want to apply the arguments
     * @param label the prefix
     * @param args the arguments you want to parse
     */
    void parseArgs(Object obj, String label, String[] args);

    /**
     * Return all Parameters using the default start arguments
     * @return returns all parameters as a map
     */
    Map<String, String> getParameters();
    /**
     * Return all Parameters with a specific label using the default start arguments
     * @return returns all parameters as a map
     */
    Map<String, String> getParameters(String label);

    /**
     * Return all Parameters using args
     * @param args the start arguments
     * @return returns all parameters as a map
     */
    Map<String, String> getParameters(String[] args);
    /**
     * Return all Parameters with a specific label using args
     * @param args the start arguments
     * @return returns all parameters as a map
     */
    Map<String, String> getParameters(String label, String[] args);

}
