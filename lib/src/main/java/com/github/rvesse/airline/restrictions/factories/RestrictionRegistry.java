package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.github.rvesse.airline.annotations.restrictions.AllowedValues;
import com.github.rvesse.airline.annotations.restrictions.RequiredOption;
import com.github.rvesse.airline.annotations.restrictions.Unrestricted;
import com.github.rvesse.airline.restrictions.IsRequired;
import com.github.rvesse.airline.restrictions.None;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class RestrictionRegistry {

    private static final Map<Class<? extends Annotation>, OptionRestrictionFactory> OPTION_RESTRICTION_FACTORIES = new HashMap<>();
    private static volatile boolean init = false;
    
    static {
        
    }
    
    static synchronized void init() {
        if (init) return;
        
        OPTION_RESTRICTION_FACTORIES.put(RequiredOption.class, new SimpleRestrictionFactory(IsRequired.class));
        OPTION_RESTRICTION_FACTORIES.put(Unrestricted.class, new SimpleRestrictionFactory(None.class));
        OPTION_RESTRICTION_FACTORIES.put(AllowedValues.class, new AllowedValuesRestrictionFactory());
        
        init = true;
    }

    public static void addOptionRestrictionFactory(Class<? extends Annotation> cls, OptionRestrictionFactory factory) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        OPTION_RESTRICTION_FACTORIES.put(cls, factory);
    }

    public static <T extends Annotation> OptionRestriction getOptionRestriction(T annotation) {
        Class<? extends Annotation> cls = annotation.getClass();
        OptionRestrictionFactory factory = OPTION_RESTRICTION_FACTORIES.get(cls);
        if (factory != null)
            return factory.createOptionRestriction(annotation);
        return null;
    }
}
