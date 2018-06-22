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
 * A resource locator that finds resources on the classpath
 * @author rvesse
 *
 */
public class ClasspathLocator implements ResourceLocator {

    public static final String CLASSPATH_URI_PREFIX = "classpath:";
    
    @Override
    public InputStream open(String searchLocation, String filename) throws IOException {
        if (searchLocation == null)
            return null;
        
        // Strip off Classpath URI prefix if present
        if (searchLocation.startsWith(CLASSPATH_URI_PREFIX))
            searchLocation = searchLocation.substring(CLASSPATH_URI_PREFIX.length());

        // Build the expected resource name
        StringBuilder resourceName = new StringBuilder();
        resourceName.append(searchLocation);
        if (!searchLocation.endsWith("/"))
            resourceName.append("/");
        resourceName.append(filename);

        // Try to open the classpath resource
        InputStream resourceStream = ClasspathLocator.class.getResourceAsStream(resourceName.toString());
        if (resourceStream != null)
            return resourceStream;

        // If the search location is not a package then return that directly if
        // it is a valid location
        if (!searchLocation.endsWith("/")) {
            InputStream locStream = ClasspathLocator.class.getResourceAsStream(searchLocation);
            if (locStream != null)
                return locStream;
        }

        return null;
    }

}
