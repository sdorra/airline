package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.parser.ParserUtil;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class SimpleRestrictionFactory implements OptionRestrictionFactory {

    private final Class<? extends OptionRestriction> cls;

    public SimpleRestrictionFactory(Class<? extends OptionRestriction> cls) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        this.cls = cls;
    }

    @Override
    public <T extends Annotation> OptionRestriction createOptionRestriction(T annotation) {
        return ParserUtil.createInstance(this.cls);
    }

}
