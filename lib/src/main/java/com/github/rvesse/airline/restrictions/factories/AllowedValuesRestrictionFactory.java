package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Locale;

import com.github.rvesse.airline.annotations.restrictions.AllowedValues;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class AllowedValuesRestrictionFactory implements OptionRestrictionFactory {

    @Override
    public <T extends Annotation> OptionRestriction createOptionRestriction(T annotation) {
        if (annotation instanceof AllowedValues) {
            AllowedValues allowedValues = (AllowedValues) annotation;
            return new com.github.rvesse.airline.restrictions.AllowedValues(allowedValues.ignoreCase(),
                    Locale.forLanguageTag(allowedValues.locale()), allowedValues.allowedValues());
        }

        return null;
    }

}
