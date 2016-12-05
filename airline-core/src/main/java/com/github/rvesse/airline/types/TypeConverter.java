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
package com.github.rvesse.airline.types;

import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;

/**
 * Interface for type converters
 * <p>
 * Type converters are used to convert the string values provided as
 * option/argument values into appropriately typed values that can be assigned
 * to the relevant option/arguments
 * </p>
 *
 */
public interface TypeConverter {

    /**
     * Convert a string value into an appropriately typed value
     * 
     * @param name
     *            Option Name
     * @param type
     *            Target Type
     * @param value
     *            String Value
     * @return Typed value
     * @exception NullPointerException
     *                Should be thrown if any of the provided arguments is null
     * @exception ParseOptionConversionException
     *                Should be thrown if the type converter cannot convert the
     *                value
     */
    public abstract Object convert(String name, Class<?> type, String value);

}