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
package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.annotations.restrictions.MutuallyExclusiveWith;
import com.github.rvesse.airline.annotations.restrictions.RequireOnlyOne;
import com.github.rvesse.airline.annotations.restrictions.RequireSome;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.options.MutuallyExclusiveRestriction;
import com.github.rvesse.airline.restrictions.options.RequireFromRestriction;

public class RequireFromRestrictionFactory implements OptionRestrictionFactory {

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        if (annotation instanceof RequireSome) {
            RequireSome some = (RequireSome) annotation;
            return new RequireFromRestriction(some.tag(), false);
        } else if (annotation instanceof RequireOnlyOne) {
            RequireOnlyOne one = (RequireOnlyOne) annotation;
            return new RequireFromRestriction(one.tag(), true);
        } else if (annotation instanceof MutuallyExclusiveWith) {
            MutuallyExclusiveWith mutExcl = (MutuallyExclusiveWith) annotation;
            return new MutuallyExclusiveRestriction(mutExcl.tag());
        }
        return null;
    }

    @Override
    public List<Class<? extends Annotation>> supportedOptionAnnotations() {
        List<Class<? extends Annotation>> supported = new ArrayList<>();
        supported.add(RequireSome.class);
        supported.add(RequireOnlyOne.class);
        supported.add(MutuallyExclusiveWith.class);
        return supported;
    }

}
