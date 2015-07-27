package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.restrictions.OptionRestriction;

/**
 * Interface for option restriction factories
 */
public interface OptionRestrictionFactory {

    /**
     * Try and create an option restriction from the given annotation
     * 
     * @param annotation
     *            Annotation
     * @return Option restriction or {@code null} if this factory cannot create
     *         a restriction from the given annotation
     */
    public abstract OptionRestriction createOptionRestriction(Annotation annotation);

}
