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

package com.github.rvesse.airline.parser.aliases.locators;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for making user alias search locations extensible e.g. supporting
 * special paths like {@code ~/} to represent the home directory
 * 
 * @author rvesse
 *
 */
public interface UserAliasSourceLocator {

    //@formatter:off
    /**
     * Default user alias source locators
     */
    public static final UserAliasSourceLocator[] DEFAULTS = 
        { 
            new WorkingDirectoryLocator(), 
            new HomeDirectoryLocator(),
            new FileLocator() 
        };
    //@formatter:on

    /**
     * Opens a search location, potentially applying some resolution rules to
     * that location
     * 
     * @param searchLocation
     *            Search location
     * @param filename
     *            Filename expected in the search location
     * @return Input stream to read the search location or {@code null} if not a
     *         valid location
     * @throws IOException
     *             Thrown if there is a problem accessing the search location
     */
    public InputStream open(String searchLocation, String filename) throws IOException;
}
