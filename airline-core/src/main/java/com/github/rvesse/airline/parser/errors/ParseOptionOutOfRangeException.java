/**
 * Copyright (C) 2010-16 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.parser.errors;

import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * Error that indicates that an options value was outside of an acceptable range
 *
 */
public class ParseOptionOutOfRangeException extends ParseRestrictionViolatedException {
    private static final long serialVersionUID = 4391651222234661142L;

    private final Object illegalValue, min, max;
    private final boolean minInclusive, maxInclusive;

    /**
     * Creates a new out of range exception
     * 
     * @param optionTitle
     *            Option title
     * @param value
     *            The value which is out of range
     * @param min
     *            Minimum value which may be null for no minimum
     * @param minInclusive
     *            Whether the minimum value is inclusive
     * @param max
     *            Maximum value which may be null for no maximum
     * @param maxInclusive
     *            Whether the maximum value is inclusive
     */
    public ParseOptionOutOfRangeException(String optionTitle, Object value, Object min, boolean minInclusive,
            Object max, boolean maxInclusive) {
        super("Value for option '%s' was given as '%s' which is not in the acceptable range: %s", optionTitle, value,
                AirlineUtils.toRangeString(min, minInclusive, max, maxInclusive));
        this.illegalValue = value;
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
    }

    public Object getIllegalValue() {
        return this.illegalValue;
    }

    public Object getMinimumValue() {
        return this.min;
    }

    public Object getMaximumValue() {
        return this.max;
    }

    public boolean isMinimumInclusive() {
        return this.minInclusive;
    }

    public boolean isMaximumInclusive() {
        return this.maxInclusive;
    }
}
