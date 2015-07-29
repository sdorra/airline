package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.rvesse.airline.annotations.restrictions.AllowedRawValues;
import com.github.rvesse.airline.annotations.restrictions.MaxOccurrences;
import com.github.rvesse.airline.annotations.restrictions.MinOccurrences;
import com.github.rvesse.airline.annotations.restrictions.Once;
import com.github.rvesse.airline.annotations.restrictions.Port;
import com.github.rvesse.airline.annotations.restrictions.RequireOnlyOne;
import com.github.rvesse.airline.annotations.restrictions.RequireSome;
import com.github.rvesse.airline.annotations.restrictions.Required;
import com.github.rvesse.airline.annotations.restrictions.RequiredOnlyIf;
import com.github.rvesse.airline.annotations.restrictions.Unrestricted;
import com.github.rvesse.airline.annotations.restrictions.ranges.ByteRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.DoubleRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.FloatRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.IntegerRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.LongRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.ShortRange;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.None;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.IsRequiredRestriction;

/**
 * Central registry for restrictions
 */
public class RestrictionRegistry {

    private static final Map<Class<? extends Annotation>, OptionRestrictionFactory> OPTION_RESTRICTION_FACTORIES = new HashMap<>();
    private static final Map<Class<? extends Annotation>, ArgumentsRestrictionFactory> ARGUMENT_RESTRICTION_FACTORIES = new HashMap<>();

    private static volatile boolean init = false;

    static {
        init();
    }
    
    private static <T extends OptionRestrictionFactory & ArgumentsRestrictionFactory> void registerCommon(Class<? extends Annotation> cls, T factory) {
        registerCommon(cls, factory, factory);
    }
    
    private static void registerCommon(Class<? extends Annotation> cls, OptionRestrictionFactory opFactory, ArgumentsRestrictionFactory argFactory) {
        OPTION_RESTRICTION_FACTORIES.put(cls, opFactory);
        ARGUMENT_RESTRICTION_FACTORIES.put(cls, argFactory);
    }

    /**
     * Initializes the default set of restrictions
     */
    static synchronized void init() {
        if (init)
            return;
        
        // Basic restrictions
        registerCommon(Required.class, new SimpleOptionRestrictionFactory(IsRequiredRestriction.class), new SimpleArgumentsRestrictionFactory(IsRequiredRestriction.class));
        registerCommon(Unrestricted.class, new SimpleOptionRestrictionFactory(None.class), new SimpleArgumentsRestrictionFactory(None.class));
        
        // Allowed values restrictions
        AllowedValuesRestrictionFactory allowedFactory = new AllowedValuesRestrictionFactory();
        registerCommon(AllowedRawValues.class, allowedFactory, allowedFactory);
        
        // Range restrictions
        RangeRestrictionFactory rangeFactory = new RangeRestrictionFactory();
        registerCommon(LongRange.class, rangeFactory);
        registerCommon(IntegerRange.class, rangeFactory);
        registerCommon(ShortRange.class, rangeFactory);
        registerCommon(ByteRange.class, rangeFactory);
        registerCommon(DoubleRange.class, rangeFactory);
        registerCommon(FloatRange.class, rangeFactory);
        
        // Advanced requirement restrictions
        RequireFromRestrictionFactory requireFactory = new RequireFromRestrictionFactory();
        OPTION_RESTRICTION_FACTORIES.put(RequireOnlyOne.class, requireFactory);
        OPTION_RESTRICTION_FACTORIES.put(RequireSome.class, requireFactory);
        OPTION_RESTRICTION_FACTORIES.put(RequiredOnlyIf.class, new RequiredOnlyIfRestrictionFactory());
        
        // Occurrences restrictions
        OccurrencesRestrictionFactory occurrenceFactory = new OccurrencesRestrictionFactory();
        registerCommon(Once.class, occurrenceFactory);
        registerCommon(MaxOccurrences.class, occurrenceFactory);
        registerCommon(MinOccurrences.class, occurrenceFactory);
        
        // Specialized restrictions
        registerCommon(Port.class, new PortRestrictionFactory());

        init = true;
    }

    /**
     * Resets the registry to its default state
     */
    public synchronized static void reset() {
        init = false;
        OPTION_RESTRICTION_FACTORIES.clear();
        ARGUMENT_RESTRICTION_FACTORIES.clear();
        init();
    }

    public static Set<Class<? extends Annotation>> getOptionRestrictionAnnotationClasses() {
        return OPTION_RESTRICTION_FACTORIES.keySet();
    }

    public static void addOptionRestriction(Class<? extends Annotation> cls, OptionRestrictionFactory factory) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        OPTION_RESTRICTION_FACTORIES.put(cls, factory);
    }

    public static <T extends Annotation> OptionRestriction getOptionRestriction(Class<? extends Annotation> cls,
            T annotation) {
        OptionRestrictionFactory factory = OPTION_RESTRICTION_FACTORIES.get(cls);
        if (factory != null)
            return factory.createOptionRestriction(annotation);
        return null;
    }

    public static void addArgumentsRestriction(Class<? extends Annotation> cls, ArgumentsRestrictionFactory factory) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        ARGUMENT_RESTRICTION_FACTORIES.put(cls, factory);
    }

    public static Set<Class<? extends Annotation>> getArgumentsRestrictionAnnotationClasses() {
        return ARGUMENT_RESTRICTION_FACTORIES.keySet();
    }

    public static <T extends Annotation> ArgumentsRestriction getArgumentsRestriction(Class<? extends Annotation> cls,
            T annotation) {
        ArgumentsRestrictionFactory factory = ARGUMENT_RESTRICTION_FACTORIES.get(cls);
        if (factory != null)
            return factory.createArgumentsRestriction(annotation);
        return null;
    }
}
