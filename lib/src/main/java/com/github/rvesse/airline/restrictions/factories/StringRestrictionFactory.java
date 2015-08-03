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
