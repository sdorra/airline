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
package com.github.rvesse.airline.types.numerics;

import java.util.HashMap;
import java.util.Map;

public class SequenceAbbreviatedNumericTypeConverter extends MapAbbreviatedNumericTypeConverter {

    public SequenceAbbreviatedNumericTypeConverter(boolean caseSensitive, Map<String, Integer> prefixes,
            long multiplierBase, String... suffixes) {
        super(caseSensitive, prefixes, buildSuffixes(multiplierBase, suffixes));
    }

    protected static Map<String, Long> buildSuffixes(long base, String... suffixes) {
        if (base <= 0)
            throw new IllegalArgumentException("Base multiplier must be >= 1");

        Map<String, Long> suffixMap = new HashMap<>();
        long m = base;
        for (String s : suffixes) {
            suffixMap.put(s, m);
            long nextM = m * base;
            if (nextM < m)
                throw new IllegalArgumentException("Too many suffixes leading to numeric overflow");
            m = nextM;
        }

        return suffixMap;
    }
}
