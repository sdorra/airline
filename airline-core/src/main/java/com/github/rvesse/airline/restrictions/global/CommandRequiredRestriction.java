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
package com.github.rvesse.airline.restrictions.global;

import java.util.List;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseCommandMissingException;
import com.github.rvesse.airline.parser.errors.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.restrictions.GlobalRestriction;

public class CommandRequiredRestriction implements GlobalRestriction {

    @Override
    public <T> void validate(ParseState<T> state) {
        CommandMetadata command = state.getCommand();
        if (command == null) {
            List<String> unparsedInput = state.getUnparsedInput();
            if (unparsedInput.isEmpty()) {
                throw new ParseCommandMissingException();
            } else {
                throw new ParseCommandUnrecognizedException(unparsedInput);
            }
        }
    }

}
