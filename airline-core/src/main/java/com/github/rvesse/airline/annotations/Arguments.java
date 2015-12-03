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
package com.github.rvesse.airline.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.annotations.restrictions.MaxOccurrences;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
@Documented
public @interface Arguments {
    /**
     * Name or names of the arguments.
     */
    String[] title() default { "" };

    /**
     * A description of the arguments.
     */
    String description() default "";

    /**
     * Argument usage for help.
     * 
     * @deprecated Not currently honoured and will be removed from 2.2.0 onwards
     */
    @Deprecated
    String usage() default "";

    /**
     * The arity of the arguments, a value of less than or equal to zero is
     * treated as unlimited
     * 
     * @deprecated Use the {@link MaxOccurrences} annotation on your fields to
     *             apply a constraint instead, will be removed from 2.2.0
     *             onwards
     */
    @Deprecated
    int arity() default Integer.MIN_VALUE;
}
