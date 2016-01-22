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
package com.github.rvesse.airline.utils.predicates.parser;

import java.util.Collection;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.utils.predicates.AbstractAbbreviationFinder;

public final class AbbreviatedOptionFinder extends AbstractAbbreviationFinder<OptionMetadata> {

    public AbbreviatedOptionFinder(String value, Collection<OptionMetadata> items) {
        super(value, items);
    }

    @Override
    protected boolean isExactNameMatch(String value, OptionMetadata item) {
        return item.getOptions().contains(value);
    }

    @Override
    protected boolean isPartialNameMatch(String value, OptionMetadata item) {
        for (String name : item.getOptions()) {
            if (name.length() <= 2)
                continue;
            if (name.startsWith(value))
                return true;
        }
        return false;
    }

}
