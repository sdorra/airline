package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.restrictions.OptionRestriction;

public interface OptionRestrictionFactory {

    public OptionRestriction createOptionRestriction(Annotation annotation);
    
}
