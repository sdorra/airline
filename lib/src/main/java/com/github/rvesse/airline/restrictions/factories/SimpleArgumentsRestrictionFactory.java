package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.parser.ParserUtil;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;

public class SimpleArgumentsRestrictionFactory implements ArgumentsRestrictionFactory {

    private final Class<? extends ArgumentsRestriction> cls;

    public SimpleArgumentsRestrictionFactory(Class<? extends ArgumentsRestriction> cls) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        this.cls = cls;
    }

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return ParserUtil.createInstance(this.cls);
    }

}
