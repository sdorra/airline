package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.annotations.restrictions.RequireOnlyOne;
import com.github.rvesse.airline.annotations.restrictions.RequireSome;
import com.github.rvesse.airline.restrictions.OptionRestriction;
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
        }
        return null;
    }

}
