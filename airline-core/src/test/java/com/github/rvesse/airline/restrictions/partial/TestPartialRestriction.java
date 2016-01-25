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

package com.github.rvesse.airline.restrictions.partial;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.testng.annotations.Test;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsIllegalValueException;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.common.AllowedRawValuesRestriction;
import com.github.rvesse.airline.restrictions.common.AllowedValuesRestriction;
import com.github.rvesse.airline.restrictions.common.IsRequiredRestriction;
import com.github.rvesse.airline.restrictions.common.NotBlankRestriction;
import com.github.rvesse.airline.restrictions.common.PartialRestriction;

public class TestPartialRestriction {

    @Command(name = "partial")
    public class Partial {

        @Option(name = "--kvp", arity = 2)
        public List<String> kvps = new ArrayList<>();

        @Arguments
        public List<String> args = new ArrayList<>();
    }

    private <T> void checkRestriction(ParseState<T> state, ArgumentsRestriction restriction,
            ArgumentsMetadata arguments, String rawValue) {
        checkRestriction(state, restriction, arguments, rawValue, rawValue);
    }

    private <T> void checkRestriction(ParseState<T> state, ArgumentsRestriction restriction,
            ArgumentsMetadata arguments, String rawValue, Object objValue) {
        restriction.preValidate(state, arguments, rawValue);
        restriction.postValidate(state, arguments, objValue);
    }

    @Test
    public void partial_notblank_01() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("not-blank");
        titles.add("blank");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new NotBlankRestriction());
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        ParseState<Partial> state = ParseState.newInstance();
        checkRestriction(state, restriction, arguments, "text");
        state = state.withArgument("text");
        checkRestriction(state, restriction, arguments, "");
        state = state.withArgument("");

        restriction.finalValidate(state, arguments);
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class, expectedExceptionsMessageRegExp = ".*'not-blank' requires a non-blank value.*")
    public void partial_notblank_02() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("not-blank");
        titles.add("blank");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new NotBlankRestriction());
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        ParseState<Partial> state = ParseState.newInstance();
        // Should fail restriction because first argument cannot be blank
        checkRestriction(state, restriction, arguments, "");
    }

    @Test
    public void partial_allowed_values_01() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("set-values");
        titles.add("any");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new AllowedValuesRestriction("foo"));
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        ParseState<Partial> state = ParseState.newInstance();
        checkRestriction(state, restriction, arguments, "foo");
        state = state.withArgument("foo");
        checkRestriction(state, restriction, arguments, "bar");
        state = state.withArgument("bar");

        restriction.finalValidate(state, arguments);
    }

    @Test(expectedExceptions = ParseArgumentsIllegalValueException.class, expectedExceptionsMessageRegExp = ".*set-values' was given as 'bar' which is not in the list of allowed values: \\[foo\\]")
    public void partial_allowed_values_02() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("set-values");
        titles.add("any");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new AllowedValuesRestriction("foo"));
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        ParseState<Partial> state = ParseState.newInstance();
        // Should fail restriction because first argument is restricted to a set
        // of values
        checkRestriction(state, restriction, arguments, "bar");
    }

    @Test
    public void partial_allowed_raw_values_01() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("set-values");
        titles.add("any");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new AllowedRawValuesRestriction(false, Locale.ENGLISH, "foo"));
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        ParseState<Partial> state = ParseState.newInstance();
        checkRestriction(state, restriction, arguments, "foo");
        state = state.withArgument("foo");
        checkRestriction(state, restriction, arguments, "bar");
        state = state.withArgument("bar");

        restriction.finalValidate(state, arguments);
    }

    @Test(expectedExceptions = ParseArgumentsIllegalValueException.class, expectedExceptionsMessageRegExp = ".*set-values' was given as 'bar' which is not in the list of allowed values: \\[foo\\]")
    public void partial_allowed_raw_values_02() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("set-values");
        titles.add("any");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new AllowedRawValuesRestriction(false, Locale.ENGLISH, "foo"));
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        ParseState<Partial> state = ParseState.newInstance();
        // Should fail restriction because first argument is restricted to a set
        // of values
        checkRestriction(state, restriction, arguments, "bar");
    }

    @Test
    public void partial_required_01() throws NoSuchFieldException, SecurityException {
        List<String> titles = new ArrayList<>();
        titles.add("not-blank");
        titles.add("blank");
        List<ArgumentsRestriction> restrictions = new ArrayList<>();
        PartialRestriction restriction = new PartialRestriction(new int[] { 0 },
                (ArgumentsRestriction) new IsRequiredRestriction());
        restrictions.add(restriction);
        List<Field> fields = Collections.singletonList(Partial.class.getField("args"));

        ArgumentsMetadata arguments = new ArgumentsMetadata(titles, "", restrictions, fields);

        // Should pass because finalValidate() restrictions are not respected by
        // partial restrictions
        ParseState<Partial> state = ParseState.newInstance();
        restriction.finalValidate(state, arguments);
    }
}
