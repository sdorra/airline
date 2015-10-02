/**
 * Copyright (C) 2010-15 the original author or authors.
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
package com.github.rvesse.airline.parser.errors;

import java.util.Collection;
import java.util.Set;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * A parser exception that relates to a restriction violated where the violation
 * pertains to some group of options
 * 
 * @author rvesse
 *
 */
public class ParseOptionGroupException extends ParseRestrictionViolatedException {
    private static final long serialVersionUID = 3018628261472277344L;
    
    private final Set<OptionMetadata> options;
    private final String tag;

    public ParseOptionGroupException(String message, String tag, Collection<OptionMetadata> options, Object... args) {
        super(message, args);
        this.options = AirlineUtils.unmodifiableSetCopy(options);
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public Set<OptionMetadata> getOptions() {
        return this.options;
    }

}