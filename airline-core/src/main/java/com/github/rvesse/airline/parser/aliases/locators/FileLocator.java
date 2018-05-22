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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Locator that does no resolution i.e. treats paths as literal files
 *
 */
public class FileLocator implements UserAliasSourceLocator {

    /**
     * Resolves the search location
     * 
     * @param searchLocation
     *            Search location
     * @return Resolved location
     */
    protected String resolve(String searchLocation) {
        return searchLocation;
    }

    @Override
    public InputStream open(String searchLocation, String filename) throws IOException {
        if (searchLocation == null)
            return null;

        // Resolve, this implementation does nothing but derived implementations
        // may override the method to provide their own resolution logic
        searchLocation = resolve(searchLocation);

        File f = new File(searchLocation);
        f = new File(f, filename);
        if (f.exists() && f.isFile() && f.canRead()) {
            return new FileInputStream(f);
        }

        return null;
    }

}
