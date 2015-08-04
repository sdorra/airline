package com.github.rvesse.airline.help.sections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.rvesse.airline.annotations.help.Discussion;
import com.github.rvesse.airline.annotations.help.Examples;
import com.github.rvesse.airline.annotations.help.ExitCodes;
import com.github.rvesse.airline.help.sections.factories.CommonSectionsFactory;
import com.github.rvesse.airline.help.sections.factories.HelpSectionFactory;

/**
 * Registry which maps annotations to help sections
 */
public class HelpSectionRegistry {

    private static final Map<Class<? extends Annotation>, HelpSectionFactory> FACTORIES = new HashMap<>();
    private static boolean init = false;

    static {
        init();
    }

    static synchronized void init() {
        if (init)
            return;

        CommonSectionsFactory commonFactory = new CommonSectionsFactory();
        FACTORIES.put(Discussion.class, commonFactory);
        FACTORIES.put(Examples.class, commonFactory);
        FACTORIES.put(ExitCodes.class, commonFactory);

        init = true;
    }

    public static synchronized void reset() {
        init = false;
        FACTORIES.clear();
        init();
    }

    public static void addFactory(Class<? extends Annotation> cls, HelpSectionFactory factory) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        FACTORIES.put(cls, factory);
    }

    public static Set<Class<? extends Annotation>> getAnnotationClasses() {
        return FACTORIES.keySet();
    }

    public static HelpSection getHelpSection(Class<? extends Annotation> cls, Annotation annotation) {
        HelpSectionFactory factory = FACTORIES.get(cls);
        if (factory != null)
            return factory.createSection(annotation);
        return null;
    }
}
