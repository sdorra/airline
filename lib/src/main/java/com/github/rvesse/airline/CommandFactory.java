package com.github.rvesse.airline;

/**
 * Interface for command factories
 *
 * @param <T>
 *            Command type
 */
public interface CommandFactory<T> {
    /**
     * Creates an instance of the given type
     * 
     * @param type
     *            Type
     * @return Instance
     */
    public abstract T createInstance(Class<?> type);
}
