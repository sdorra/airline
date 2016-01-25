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
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

/**
 * Abstract base class for parsers providing some utility methods
 */
public class AbstractParser<T> {

    /**
     * Default type converter
     */
    private static final TypeConverter DEFAULT_TYPE_CONVERTER = new DefaultTypeConverter();

    protected final void checkValidValue(ParseState<T> state, ArgumentsMetadata args, String tokenStr) {
        for (ArgumentsRestriction restriction : args.getRestrictions()) {
            restriction.preValidate(state, args, tokenStr);
        }
    }

    /**
     * Checks for a valid value (prior to type conversion) and throws an error
     * if the value for the option fails a restriction
     * 
     * @param state
     *            Parser state
     * @param option
     *            Option meta-data
     * @param tokenStr
     *            Token string
     */
    protected final void checkValidValue(ParseState<T> state, OptionMetadata option, String tokenStr) {
        for (OptionRestriction restriction : option.getRestrictions()) {
            restriction.preValidate(state, option, tokenStr);
        }
    }

    /**
     * Checks for a valid value (after type conversion) and throws an error if
     * the value for the option fails a restriction
     * 
     * @param state
     *            Parser state
     * @param option
     *            Option meta-data
     * @param value
     *            Converted value
     */
    protected final void checkValidConvertedValue(ParseState<T> state, OptionMetadata option, Object value) {
        for (OptionRestriction restriction : option.getRestrictions()) {
            restriction.postValidate(state, option, value);
        }
    }

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