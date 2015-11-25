/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

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

    /**
     * Initializes the default set of restrictions
     */
    static synchronized void init() {
        if (init)
            return;

        // Use ServerLoader to obtain restrictions
        ServiceLoader<OptionRestrictionFactory> optionRestrictionFactories = ServiceLoader
                .load(OptionRestrictionFactory.class);
        for (OptionRestrictionFactory factory : optionRestrictionFactories) {
            for (Class<? extends Annotation> cls : factory.supportedOptionAnnotations()) {
                OPTION_RESTRICTION_FACTORIES.put(cls, factory);
            }
        }
        ServiceLoader<ArgumentsRestrictionFactory> argumentsRestrictionFactories = ServiceLoader
                .load(ArgumentsRestrictionFactory.class);
        for (ArgumentsRestrictionFactory factory : argumentsRestrictionFactories) {
            for (Class<? extends Annotation> cls : factory.supportedArgumentsAnnotations()) {
                ARGUMENT_RESTRICTION_FACTORIES.put(cls, factory);
            }
        }

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
