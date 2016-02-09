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
package com.github.rvesse.airline.maven.formats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public class FormatMappingRegistry {

    private static Map<String, FormatProvider> providers = new HashMap<>();

    static {
        ServiceLoader<FormatProvider> providerLoader = ServiceLoader.load(FormatProvider.class);
        Iterator<FormatProvider> iter = providerLoader.iterator();
        while (iter.hasNext()) {
            add(iter.next());
        }
    }

    /**
     * Defines a mapping for a format provider
     * <p>
     * The mapping will be associated with the format name given by the
     * {@link FormatProvider#getDefaultMappingName()} method.
     * </p>
     * 
     * @param provider
     *            Format provider
     */
    public static void add(FormatProvider provider) {
        if (provider == null)
            return;
        add(provider.getDefaultMappingName(), provider);
    }

    /**
     * Defines a mapping of a format to a format provider
     * <p>
     * If the given {@code provider} is {@code null} then the effect is to
     * remove the mapping for the given format
     * </p>
     * 
     * @param format
     *            Format name
     * @param provider
     *            Format provider
     */
    public static void add(String format, FormatProvider provider) {
        providers.put(format, provider);
    }

    /**
     * Removes the mapping for a format
     * 
     * @param format
     *            Format name
     */
    public static void remove(String format) {
        providers.remove(format);
    }

    /**
     * Finds a format provider for the given format
     * 
     * @param format
     *            Format name
     * @return Format provider if available or {@code null} if no mapping
     *         defined
     */
    public static FormatProvider find(String format) {
        return providers.get(format);
    }

    /**
     * Gets the available format names
     * 
     * @return
     */
    public static Set<String> availableFormatNames() {
        return Collections.unmodifiableSet(providers.keySet());
    }
}
