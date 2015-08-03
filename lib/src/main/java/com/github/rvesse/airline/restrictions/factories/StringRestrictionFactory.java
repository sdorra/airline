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

import com.github.rvesse.airline.annotations.restrictions.MaxLength;
import com.github.rvesse.airline.annotations.restrictions.MinLength;
import com.github.rvesse.airline.annotations.restrictions.NotBlank;
import com.github.rvesse.airline.annotations.restrictions.NotEmpty;
import com.github.rvesse.airline.annotations.restrictions.Pattern;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.LengthRestriction;
import com.github.rvesse.airline.restrictions.common.NotBlankRestriction;
import com.github.rvesse.airline.restrictions.common.NotEmptyRestriction;
import com.github.rvesse.airline.restrictions.common.PatternRestriction;

public class StringRestrictionFactory implements ArgumentsRestrictionFactory, OptionRestrictionFactory {

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    protected AbstractCommonRestriction createCommon(Annotation annotation) {
        if (annotation instanceof Pattern) {
            Pattern pattern = (Pattern) annotation;
            return new PatternRestriction(pattern.pattern(), pattern.flags());
        } else if (annotation instanceof MaxLength) {
            MaxLength ml = (MaxLength) annotation;
            return new LengthRestriction(ml.length(), true);
        } else if (annotation instanceof MinLength) {
            MinLength ml = (MinLength) annotation;
            return new LengthRestriction(ml.length(), false);
        } else if (annotation instanceof NotBlank) {
            return new NotBlankRestriction();
        } else if (annotation instanceof NotEmpty) {
            return new NotEmptyRestriction();
        }
        return null;
    }
}
