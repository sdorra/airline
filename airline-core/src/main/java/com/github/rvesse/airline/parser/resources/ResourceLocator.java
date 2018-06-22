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

package com.github.rvesse.airline.parser.resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for making resource search locations extensible e.g. supporting
 * special paths like {@code ~/} to represent the home directory
 * 
 * @author rvesse
 *
 */
public interface ResourceLocator {

    /**
     * Opens a search location, potentially applying some resolution rules to
     * that location
     * <p>
     * If the given {@code resourceName} exists in the search location then that
     * resource should be returned. If that is not a valid resource but the
     * search location itself is a valid resource then the locator should return
     * that instead.
     * </p>
     * 
     * @param searchLocation
     *            Search location
     * @param resourceName
     *            Resource name expected in the search location
     * @return Input stream to read the search location or {@code null} if not a
     *         valid location
     * @throws IOException
     *             Thrown if there is a problem accessing the search location
     */
    public InputStream open(String searchLocation, String resourceName) throws IOException;
}
