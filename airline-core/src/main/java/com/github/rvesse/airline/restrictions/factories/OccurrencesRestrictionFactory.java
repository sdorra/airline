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
import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.annotations.restrictions.MaxOccurrences;
import com.github.rvesse.airline.annotations.restrictions.MinOccurrences;
import com.github.rvesse.airline.annotations.restrictions.Once;
import com.github.rvesse.airline.annotations.restrictions.Required;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.OccurrencesRestriction;

/**
 * An annotation used to mark that an option must occur at most once
 * 
 * <p>
 * To more finely control the number of occurrences of an option use
 * {@link MinOccurrences} and {@link MaxOccurrences}. If you simply wish to
 * state that an option must occur then use {@link Required}.
 * </p>
 * 
 * @author rvesse
 *
 */
public class OccurrencesRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    protected OccurrencesRestriction createCommon(Annotation annotation) {
        if (annotation instanceof MaxOccurrences) {
            MaxOccurrences max = (MaxOccurrences) annotation;
            return new OccurrencesRestriction(max.occurrences(), true);
        } else if (annotation instanceof MinOccurrences) {
            MinOccurrences min = (MinOccurrences) annotation;
            return new OccurrencesRestriction(min.occurrences(), false);
        } else if (annotation instanceof Once) {
            return new OccurrencesRestriction(1, true);
        }
        return null;
    }
    
    protected List<Class<? extends Annotation>> supportedAnnotations() {
        List<Class<? extends Annotation>> supported = new ArrayList<>();
        supported.add(MaxOccurrences.class);
        supported.add(MinOccurrences.class);
        supported.add(Once.class);
        return supported;
    }

    @Override
    public List<Class<? extends Annotation>> supportedArgumentsAnnotations() {
        return supportedAnnotations();
    }

    @Override
    public List<Class<? extends Annotation>> supportedOptionAnnotations() {
        return supportedAnnotations();
    }

}
