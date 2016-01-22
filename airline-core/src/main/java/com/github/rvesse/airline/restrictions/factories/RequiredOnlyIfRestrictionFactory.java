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
package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import com.github.rvesse.airline.annotations.restrictions.RequiredOnlyIf;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.options.RequiredOnlyIfRestriction;

public class RequiredOnlyIfRestrictionFactory implements OptionRestrictionFactory {

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        if (annotation instanceof RequiredOnlyIf) {
            RequiredOnlyIf onlyIf = (RequiredOnlyIf) annotation;
            return new RequiredOnlyIfRestriction(onlyIf.names());
        }
        return null;
    }

    @Override
    public List<Class<? extends Annotation>> supportedOptionAnnotations() {
        return Collections.<Class<? extends Annotation>>singletonList(RequiredOnlyIf.class);
    }

}
