package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.restrictions.ArgumentsRestriction;

/**
 * Interface for arguments restriction factories
 */
public interface ArgumentsRestrictionFactory {

    /**
     * Tries to create an arguments restriction from the given annotation
     * 
     * @param annotation
     *            Annotation
     * @return Arguments restriction or {@code null} if this factory cannot
     *         create a restriction from the given annotation
     */
    public abstract ArgumentsRestriction createArgumentsRestriction(Annotation annotation);

}
