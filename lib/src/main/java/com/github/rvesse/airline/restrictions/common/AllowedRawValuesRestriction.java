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

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsIllegalValueException;
import com.github.rvesse.airline.parser.errors.ParseOptionIllegalValueException;
import com.github.rvesse.airline.restrictions.AbstractRestriction;
import com.github.rvesse.airline.utils.predicates.LocaleSensitiveStringFinder;

public class AllowedRawValuesRestriction extends AbstractRestriction {

    private final Set<String> allowedValues = new LinkedHashSet<String>();
    private final Locale locale;
    private final boolean caseInsensitive;

    public AllowedRawValuesRestriction(boolean ignoreCase, Locale locale, String... values) {
        if (locale == null)
            locale = Locale.ENGLISH;
        this.locale = locale;
        this.caseInsensitive = ignoreCase;
        for (String value : values) {
            if (ignoreCase)
                value = value.toLowerCase(locale);
            allowedValues.add(value);
        }
    }

    public Set<String> getAllowedValues() {
        return SetUtils.unmodifiableSet(this.allowedValues);
    }

    public boolean ignoresCase() {
        return this.caseInsensitive;
    }

    public Locale usesLocale() {
        return locale;
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        // Not enforced if no values specified
        if (allowedValues.isEmpty())
            return;

        // Check in list of values
        if (!CollectionUtils.exists(this.allowedValues, new LocaleSensitiveStringFinder(value, this.locale)))
            throw new ParseOptionIllegalValueException(option.getTitle(), value, asObjects(allowedValues));
    }

    private static Set<Object> asObjects(Set<String> set) {
        Set<Object> newSet = new LinkedHashSet<Object>();
        for (String item : set) {
            newSet.add((Object) item);
        }
        return newSet;
    }

    @Override
    public <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        // Not enforced if no values specified
        if (allowedValues.isEmpty())
            return;

        // Check in list of values
        if (!CollectionUtils.exists(this.allowedValues, new LocaleSensitiveStringFinder(value, this.locale))) {
            // Determine appropriate title
            String title;
            if (state.getParsedArguments().size() == 0) {
                title = arguments.getTitle().get(0);
            } else if (state.getParsedArguments().size() < arguments.getTitle().size()) {
                title = arguments.getTitle().get(state.getParsedArguments().size());
            } else {
                title = arguments.getTitle().get(arguments.getTitle().size() - 1);
            }
            throw new ParseArgumentsIllegalValueException(title, value, asObjects(allowedValues));
        }
    }
}
