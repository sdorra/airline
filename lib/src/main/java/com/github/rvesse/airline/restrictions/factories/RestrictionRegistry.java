package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.rvesse.airline.annotations.restrictions.AllowedRawValues;
import com.github.rvesse.airline.annotations.restrictions.IntegerRange;
import com.github.rvesse.airline.annotations.restrictions.Required;
import com.github.rvesse.airline.annotations.restrictions.Unrestricted;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.IsRequiredRestriction;
import com.github.rvesse.airline.restrictions.None;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class RestrictionRegistry {

    private static final Map<Class<? extends Annotation>, OptionRestrictionFactory> OPTION_RESTRICTION_FACTORIES = new HashMap<>();
    private static final Map<Class<? extends Annotation>, ArgumentsRestrictionFactory> ARGUMENT_RESTRICTION_FACTORIES = new HashMap<>();
    private static volatile boolean init = false;
    
    static {
        init();
    }
    
    static synchronized void init() {
        if (init) return;
        
        OPTION_RESTRICTION_FACTORIES.put(Required.class, new SimpleOptionRestrictionFactory(IsRequiredRestriction.class));
        OPTION_RESTRICTION_FACTORIES.put(Unrestricted.class, new SimpleOptionRestrictionFactory(None.class));
        OPTION_RESTRICTION_FACTORIES.put(AllowedRawValues.class, new AllowedValuesRestrictionFactory());
        OPTION_RESTRICTION_FACTORIES.put(IntegerRange.class, new IntegerRangeRestrictionFactory());
        
        ARGUMENT_RESTRICTION_FACTORIES.put(Required.class, new SimpleArgumentsRestrictionFactory(IsRequiredRestriction.class));
        ARGUMENT_RESTRICTION_FACTORIES.put(Unrestricted.class, new SimpleArgumentsRestrictionFactory(None.class));
        
        init = true;
    }
    
    public synchronized static void reset() {
        init = false;
        OPTION_RESTRICTION_FACTORIES.clear();
        ARGUMENT_RESTRICTION_FACTORIES.clear();
        init();
    }
    
    public static Set<Class<? extends Annotation>> getOptionRestrictionAnnotations() {
        return OPTION_RESTRICTION_FACTORIES.keySet();
    }

    public static void addOptionRestrictionFactory(Class<? extends Annotation> cls, OptionRestrictionFactory factory) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        OPTION_RESTRICTION_FACTORIES.put(cls, factory);
    }

    public static <T extends Annotation> OptionRestriction getOptionRestriction(Class<? extends Annotation> cls, T annotation) {
        OptionRestrictionFactory factory = OPTION_RESTRICTION_FACTORIES.get(cls);
        if (factory != null)
            return factory.createOptionRestriction(annotation);
        return null;
    }
    
    public static Set<Class<? extends Annotation>> getArgumentsRestrictionAnnotations() {
        return ARGUMENT_RESTRICTION_FACTORIES.keySet();
    }
    
    public static <T extends Annotation> ArgumentsRestriction getArgumentsRestriction(Class<? extends Annotation> cls, T annotation) {
        ArgumentsRestrictionFactory factory = ARGUMENT_RESTRICTION_FACTORIES.get(cls);
        if (factory != null)
            return factory.createArgumentsRestriction(annotation);
        return null;
    }
}
