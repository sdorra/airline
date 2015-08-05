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
package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.global.CommandRequiredRestriction;
import com.github.rvesse.airline.restrictions.global.NoMissingOptionValuesRestriction;
import com.github.rvesse.airline.restrictions.global.NoUnexpectedArgumentsRestriction;

/**
 * Interface for restrictions
 */
public interface GlobalRestriction {
    
    //@formatter:off
    static final GlobalRestriction[] DEFAULTS = new GlobalRestriction[] {
        new CommandRequiredRestriction(),
        new NoUnexpectedArgumentsRestriction(),
        new NoMissingOptionValuesRestriction()
    };
    //@formatter:on
    
    /**
     * Validates the parser state
     * <p>
     * Should throw an exception if the restriction is violated, otherwise
     * should simply return
     * </p>
     * 
     * @param state
     *            Parser state
     */
    public abstract <T> void validate(ParseState<T> state);
}
