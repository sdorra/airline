package com.github.rvesse.airline.io;

/**
 * Interface for classes that provide control codes
 *
 * @param <T>
 *            Attribute source
 */
public interface ControlCodeSource<T> {

    /**
     * Translates the attribute source into a control code that can be passed to
     * an input/output stream
     * 
     * @param attributeSource
     *            Attribute source
     * @return Control code
     */
    public String getControlCode(T attributeSource);

    /**
     * Gets a reset code that can be used to reset any changes previously made
     * by the given attribute
     * 
     * @param attributeSource
     *            Attribute source
     * 
     * @return Control code
     */
    public String getResetControlCode(T attributeSource);

    /**
     * Gets a reset code that can be used to reset any changes previously made
     * by any attributes of the type supported by this source
     * 
     * @return Control Code
     */
    public String getFullResetControlCode();
}
