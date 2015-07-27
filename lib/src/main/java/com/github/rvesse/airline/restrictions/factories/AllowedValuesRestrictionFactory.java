package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Locale;

import com.github.rvesse.airline.annotations.restrictions.AllowedRawValues;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class AllowedValuesRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        return (OptionRestriction) createCommon(annotation);
    }

    protected Object createCommon(Annotation annotation) {
        if (annotation instanceof AllowedRawValues) {
            AllowedRawValues allowedValues = (AllowedRawValues) annotation;
            return new com.github.rvesse.airline.restrictions.common.AllowedRawValuesRestriction(allowedValues.ignoreCase(),
                    Locale.forLanguageTag(allowedValues.locale()), allowedValues.allowedValues());
        }

        return null;
    }

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return (ArgumentsRestriction) createCommon(annotation);
    }
}
