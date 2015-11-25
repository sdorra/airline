/**
 * Copyright (C) 2010-15 the original author or authors.
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
package com.github.rvesse.airline.restrictions.common;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.DefaultTypeConverter;
import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsIllegalValueException;
import com.github.rvesse.airline.parser.errors.ParseInvalidRestrictionException;
import com.github.rvesse.airline.parser.errors.ParseOptionIllegalValueException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;

public class AllowedValuesRestriction extends AbstractAllowedValuesRestriction {

    public AllowedValuesRestriction(String... rawValues) {
        super(false);
        this.rawValues.addAll(AirlineUtils.arrayToList(rawValues));
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {
        // Not enforced if no values specified
        if (this.rawValues.isEmpty())
            return;

        Collection<Pair<OptionMetadata, Object>> parsedOptions = CollectionUtils.select(state.getParsedOptions(),
                new ParsedOptionFinder(option));
        if (parsedOptions.isEmpty())
            return;

        // Else need to convert the raw values to their actual values
        Set<Object> allowedValues = createAllowedValues(state, option.getTitle(), option.getJavaType());

        for (Pair<OptionMetadata, Object> parsedOption : parsedOptions) {
            if (!allowedValues.contains(parsedOption.getRight()))
                throw new ParseOptionIllegalValueException(option.getTitle(), parsedOption.getRight(), allowedValues);
        }
    }

    protected <T> Set<Object> createAllowedValues(ParseState<T> state, String title, Class<?> type) {
        Set<Object> actualValues = new LinkedHashSet<Object>();
        TypeConverter converter = state.getParserConfiguration().getTypeConverter();
        if (converter == null)
            converter = new DefaultTypeConverter();
        for (String rawValue : this.rawValues) {
            try {
                actualValues.add(converter.convert(title, type, rawValue));
            } catch (Exception e) {
                throw new ParseInvalidRestrictionException(e,
                        "Unable to parse raw value '%s' in order to apply allowed values restriction", rawValue);
            }
        }
        return actualValues;
    }

    @Override
    public <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments) {
        // Not enforced if no values specified
        if (this.rawValues.isEmpty())
            return;

        List<Object> parsedArguments = state.getParsedArguments();
        if (parsedArguments.isEmpty())
            return;

        Set<Object> allowedValues = createAllowedValues(state, arguments.getTitle().get(0), arguments.getJavaType());
        int i = 0;
        for (Object parsedArg : parsedArguments) {
            if (!allowedValues.contains(parsedArg))
                throw new ParseArgumentsIllegalValueException(AbstractCommonRestriction.getArgumentTitle(arguments, i),
                        parsedArg, allowedValues);
            i++;
        }
    }

}
