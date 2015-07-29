package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

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

}
