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

package com.github.rvesse.airline.restrictions.common;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;

public class PartialRestriction extends AbstractCommonRestriction {

    private final Set<Integer> indices = new TreeSet<>();
    private final OptionRestriction optionRestriction;
    private final ArgumentsRestriction argumentsRestriction;

    public PartialRestriction(int[] indices, OptionRestriction optionRestriction) {
        for (int i : indices) {
            this.indices.add(i);
        }
        this.optionRestriction = optionRestriction;
        this.argumentsRestriction = optionRestriction instanceof ArgumentsRestriction
                ? (ArgumentsRestriction) optionRestriction : null;
    }

    public PartialRestriction(int[] indices, ArgumentsRestriction argumentsRestriction) {
        for (int i : indices) {
            this.indices.add(i);
        }
        this.optionRestriction = argumentsRestriction instanceof OptionRestriction
                ? (OptionRestriction) argumentsRestriction : null;
        this.argumentsRestriction = argumentsRestriction;
    }

    private <T> boolean isApplicableToOption(ParseState<T> state, OptionMetadata option) {
        int index = CollectionUtils.countMatches(state.getParsedOptions(), new ParsedOptionFinder(option));
        return indices.contains(index);
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        if (optionRestriction == null)
            return;
        if (!isApplicableToOption(state, option))
            return;

        this.optionRestriction.preValidate(state, option, value);
    }
    
    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option, Object value) {
        if (optionRestriction == null)
            return;
        if (!isApplicableToOption(state, option))
            return;

        this.optionRestriction.postValidate(state, option, value);
    }

    private <T> boolean isApplicableToArgument(ParseState<T> state) {
        int index = state.getParsedArguments().size();
        return indices.contains(index);
    }

    @Override
    public <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        if (argumentsRestriction == null)
            return;
        if (!isApplicableToArgument(state))
            return;

        this.argumentsRestriction.preValidate(state, arguments, value);
    }
    
    @Override
    public <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments, Object value) {
        if (argumentsRestriction == null)
            return;
        if (!isApplicableToArgument(state))
            return;

        this.argumentsRestriction.postValidate(state, arguments, value);
    }
}
