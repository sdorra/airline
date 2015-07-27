package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Locale;

import com.github.rvesse.airline.annotations.restrictions.AllowedRawValues;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class AllowedValuesRestrictionFactory implements OptionRestrictionFactory {

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        if (annotation instanceof AllowedRawValues) {
            AllowedRawValues allowedValues = (AllowedRawValues) annotation;
            return new com.github.rvesse.airline.restrictions.AllowedRawValuesRestriction(allowedValues.ignoreCase(),
                    Locale.forLanguageTag(allowedValues.locale()), allowedValues.allowedValues());
        }

        return null;
    }
}
