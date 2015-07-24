package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.restrictions.ArgumentsRestriction;

public interface ArgumentsRestrictionFactory {

    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation);
    
}
