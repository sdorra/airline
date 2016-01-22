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

import org.testng.annotations.Test;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.common.AllowedValuesRestriction;
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

    @Test
    public void partial_01() throws NoSuchFieldException, SecurityException {
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
        restriction.preValidate(state, arguments, "text");
        state = state.withArgument("text");
        restriction.preValidate(state, arguments, "");
        state = state.withArgument("");

        restriction.postValidate(state, arguments);
    }

    // Currently does not work because postValidate() is too general and sees
    // the final parser state not the intermediate parser state. Need to change
    // method signatures so there is a postValidate() and a finalValidate()
    @Test(enabled = false)
    public void partial_02() throws NoSuchFieldException, SecurityException {
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
        restriction.preValidate(state, arguments, "foo");
        state = state.withArgument("foo");
        restriction.preValidate(state, arguments, "");
        state = state.withArgument("bar");

        restriction.postValidate(state, arguments);
    }
}
