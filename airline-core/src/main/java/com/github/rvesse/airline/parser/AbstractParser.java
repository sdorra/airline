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
package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.DefaultTypeConverter;

/**
 * Abstract base class for parsers providing some utility methods
 */
public class AbstractParser<T> {

    /**
     * Default type converter
     */
    private static final TypeConverter DEFAULT_TYPE_CONVERTER = new DefaultTypeConverter();

    /**
     * Gets the type converter to use for converting arguments into
     * option/argument values
     * 
     * @param state
     *            Parser State
     * @return Type converter
     */
    protected final TypeConverter getTypeConverter(ParseState<T> state) {
        if (state != null) {
            return state.getParserConfiguration().getTypeConverter();
        } else {
            return DEFAULT_TYPE_CONVERTER;
        }
    }

}