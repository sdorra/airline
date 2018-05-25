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
package com.github.rvesse.airline.parser.errors.handlers;

import java.util.Collection;

import com.github.rvesse.airline.parser.ParseResult;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseException;

/**
 * An error handler which collects all errors during parsing and then fails at
 * the end of parsing
 * 
 * @author rvesse
 *
 */
public class FailAll extends AbstractCollectingHandler {

    @Override
    public <T> ParseResult<T> finished(ParseState<T> state) {
        Collection<ParseException> errors = getCollection();
        if (errors.size() == 1) {
            // Single error handled, throw as-is
            throw errors.iterator().next();
        } else if (errors.size() > 1) {
            // Multiple errors handled, throw aggregation noting suppressed
            // errors
            ParseException aggEx = new ParseException(
                    "Parsing encountered %d errors, see suppressed errors for details", errors.size());
            for (ParseException e : errors) {
                aggEx.addSuppressed(e);
            }
            throw aggEx;
        } else {
            // Can only reach here if there were no errors handled i.e. no
            // errors!
            return new ParseResult<>(state, null);
        }
    }

}
