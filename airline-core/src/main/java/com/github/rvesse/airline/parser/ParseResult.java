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

import java.util.Collection;
import java.util.Collections;

import com.github.rvesse.airline.parser.errors.ParseException;

/**
 * Represents parsing results
 * 
 * @author rvesse
 *
 * @param <T>
 *            Command type
 */
public class ParseResult<T> {
    private final ParseState<T> state;
    private final Collection<ParseException> errors;

    public ParseResult(ParseState<T> state, Collection<ParseException> errors) {
        if (state == null)
            throw new NullPointerException("state cannot be null");
        this.state = state;
        this.errors = errors != null ? Collections.<ParseException> unmodifiableCollection(errors)
                : Collections.<ParseException> emptyList();
    }

    /**
     * Indicates whether parsing was successful
     * 
     * @return True if successful, false if any errors occurred
     */
    public boolean wasSuccessful() {
        return this.errors.size() == 0;
    }

    /**
     * Gets the final parser state
     * 
     * @return Parser state
     */
    public ParseState<T> getState() {
        return this.state;
    }

    /**
     * Gets the collection of errors that occurred, may be empty if parsing was
     * successful
     * 
     * @return Errors
     */
    public Collection<ParseException> getErrors() {
        return this.errors;
    }
}
