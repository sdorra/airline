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

import com.github.rvesse.airline.help.sections.HelpSection;

public class HelpSectionComparator implements Comparator<HelpSection> {

    @Override
    public int compare(HelpSection o1, HelpSection o2) {
        if (o1 == null) {
            if (o2 == null)
                return 0;
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        int comparison = Integer.compare(o1.suggestedOrder(), o2.suggestedOrder());
        if (comparison == 0) {
            if (o1.getTitle() == null) {
                if (o2.getTitle() != null) {
                    comparison = -1;
                }
            } else if (o2.getTitle() == null) {
                comparison = 1;
            } else {
                comparison = o1.getTitle().compareTo(o2.getTitle());
            }
        }
        return comparison;
    }

}
