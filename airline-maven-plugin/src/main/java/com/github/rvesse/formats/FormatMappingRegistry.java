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
package com.github.rvesse.formats;

import java.util.HashMap;
import java.util.Map;

public class FormatMappingRegistry {

    private static Map<String, FormatProvider> providers = new HashMap<>();
    
    static {
        addMapping(new CliFormatProvider());
        addMapping(new ManFormatProvider());
    }
    
    public static void addMapping(FormatProvider provider) {
        addMapping(provider.getDefaultMappingName(), provider);
    }
    
    public static void addMapping(String format, FormatProvider provider) {
        providers.put(format, provider);
    }
    
    public static FormatProvider find(String format) {
        return providers.get(format);
    }
}
