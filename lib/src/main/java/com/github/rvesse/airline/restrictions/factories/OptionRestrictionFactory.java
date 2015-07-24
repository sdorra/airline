package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.restrictions.OptionRestriction;

public interface OptionRestrictionFactory {

    public <T extends Annotation> OptionRestriction createOptionRestriction(T annotation);
}
