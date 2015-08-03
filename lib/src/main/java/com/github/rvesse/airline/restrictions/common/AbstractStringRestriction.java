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

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;

public abstract class AbstractStringRestriction extends AbstractCommonRestriction {

    @Override
    public final <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        if (!isValid(value))
            throw violated(state, option, value);
    }

    @Override
    public final <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        if (!isValid(value))
            throw violated(state, arguments, value);
    }

    protected abstract boolean isValid(String value);

    protected abstract <T> ParseRestrictionViolatedException violated(ParseState<T> state, OptionMetadata option,
            String value);

    protected abstract <T> ParseRestrictionViolatedException violated(ParseState<T> state, ArgumentsMetadata arguments,
            String value);
}
