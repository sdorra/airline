package com.github.rvesse.airline;

/**
 * Helper class used to represent the result of an attempted conversion.
 * Primarily used internally by {@link DefaultTypeConverter} but may be
 * generally useful for implementing custom {@link TypeConverter}
 * implementations or extending the {@linkplain DefaultTypeConverter}
 *
 */
public class ConvertResult {
    private final Object value;
    private final boolean success;

    /**
     * Special constant instance used to indicate failure
     */
    public static final ConvertResult FAILURE = new ConvertResult();

    private ConvertResult() {
        this.value = null;
        this.success = false;
    }

    /**
     * Creates a new conversion result that indicates success
     * 
     * @param value
     *            Converted value
     */
    public ConvertResult(Object value) {
        this.value = value;
        this.success = true;
    }

    /**
     * Whether the conversion was successful
     * 
     * @return True if successful, false otherwise
     */
    public boolean wasSuccessfull() {
        return this.success;
    }

    /**
     * The converted value
     * 
     * @return Converted value
     */
    public Object getConvertedValue() {
        return this.value;
    }
}