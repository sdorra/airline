package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.annotations.restrictions.MaxOccurrences;
import com.github.rvesse.airline.annotations.restrictions.MinOccurrences;
import com.github.rvesse.airline.annotations.restrictions.Once;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.OccurrencesRestriction;

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

}
