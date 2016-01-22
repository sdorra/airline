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
package com.github.rvesse.airline.utils.comparators;

import java.util.Comparator;

public class StringHierarchyComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if (o1 == o2)
            return 0;

        if (o1 == null) {
            if (o2 == null)
                return 0;
            return -1;
        } else if (o2 == null) {
            return 1;
        }
        
        int c = Integer.compare(o1.length(), o2.length());
        if (c == 0) {
            c = o1.compareTo(o2);
        }
        return c;
    }

}
