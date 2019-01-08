package me.david.automatisation.generators;

/**
 * Generates something
 * @param <T> something
 */
public interface Generator<T> {

    /*
     * Generates a value
     */
    T getRandom();

}
