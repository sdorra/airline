package com.github.rvesse.airline.builder;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Abstract base class for builders
 *
 * @param <T>
 *            Type that the builder will produce
 */
public abstract class AbstractBuilder<T> {

    /**
     * Checks a value given for a parameter is not null
     * 
     * @param value
     *            Value
     * @param paramName
     *            Parameter
     */
    protected final void checkNotNull(String value, String paramName) {
        Preconditions.checkNotNull(value, "%s cannot be null", paramName);
    }

    /**
     * Checks a value given for a parameter is not null/empty
     * 
     * @param value
     *            Value
     * @param paramName
     *            Parameter
     */
    protected final void checkNotEmpty(String value, String paramName) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(value), "%s cannot be null/empty", paramName);
    }

    /**
     * Checks a value given for a parameter is not blank i.e. not null, empty or
     * all whitespace
     * 
     * @param value
     *            Value
     * @param paramName
     *            Parameter
     */
    protected final void checkNotBlank(String value, String paramName) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(value) && !StringUtils.isWhitespace(value),
                "%s cannot be null/empty/whitespace", paramName);
    }

    /**
     * Builds the type
     * 
     * @return Type instance
     */
    public abstract T build();
}
