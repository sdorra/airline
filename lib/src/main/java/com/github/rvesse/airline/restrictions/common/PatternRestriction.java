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

import java.util.regex.Pattern;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * A restriction which requires the raw values to match a given regular
 * expression
 */
public class PatternRestriction extends AbstractCommonRestriction {

    private final Pattern pattern;

    public PatternRestriction(String pattern, int flags) {
        this.pattern = Pattern.compile(pattern, flags);
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        if (!this.pattern.matcher(value).find())
            throw new ParseRestrictionViolatedException(
                    "Option '%s' was given value '%s' which does not match the regular expression '%s'",
                    option.getTitle(), value, this.pattern.toString());
    }

    @Override
    public <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        if (!this.pattern.matcher(value).find())
            throw new ParseRestrictionViolatedException(
                    "Argument '%s' was given value '%s' which does not match the regular expression '%s'",
                    AirlineUtils.first(arguments.getTitle()), value, this.pattern.toString());
    }

}
