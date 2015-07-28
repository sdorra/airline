package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.annotations.restrictions.Port;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.PortRestriction;

public class PortRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        if (annotation instanceof Port) {
            return createCommon((Port) annotation);
        }
        return null;
    }

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        if (annotation instanceof Port) {
            return createCommon((Port) annotation);
        }
        return null;
    }

    protected final PortRestriction createCommon(Port annotation) {
        return new PortRestriction(annotation.acceptablePorts());
    }
}
