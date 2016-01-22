/**
 * Copyright (C) 2010-16 the original author or authors.
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
package com.github.rvesse.airline.help.sections.factories;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import com.github.rvesse.airline.help.sections.HelpSection;

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

        ServiceLoader<HelpSectionFactory> helpSectionFactories = ServiceLoader.load(HelpSectionFactory.class);
        for (HelpSectionFactory factory : helpSectionFactories) {
            for (Class<? extends Annotation> cls : factory.supportedAnnotations()) {
                FACTORIES.put(cls, factory);
            }
        }

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
