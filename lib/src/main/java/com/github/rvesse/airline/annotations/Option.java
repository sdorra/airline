/**
 * Copyright (C) 2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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

import com.github.rvesse.airline.CompletionBehaviour;
import com.github.rvesse.airline.model.OptionMetadata;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Annotation to mark a field as an option
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Option {
    /**
     * Is this a command, group or global option
     */
    OptionType type() default OptionType.COMMAND;

    /**
     * Name use to identify the option value in documentation and error
     * messages.
     */
    String title() default "";

    /**
     * An array of allowed command line parameters (e.g. "-n", "--name",
     * etc...).
     */
    String[] name();

    /**
     * A description of this option.
     */
    String description() default "";

    /**
     * Whether this option is required.
     */
    boolean required() default false;

    /**
     * How many parameter values this option will consume. For example, an arity
     * of 2 will allow "-pair value1 value2".
     */
    int arity() default Integer.MIN_VALUE;

    /**
     * If true, this parameter won't appear in the usage().
     */
    boolean hidden() default false;

    /**
     * If true this parameter can override parameters of the same name (set via
     * the {@link Option#name()} property) declared by parent classes assuming
     * the option definitions are compatible.
     * <p>
     * See
     * {@link OptionMetadata#override(java.util.Set, OptionMetadata, OptionMetadata)}
     * for legal overrides
     * </p>
     * 
     * @return True if an override, false otherwise
     */
    boolean override() default false;

    /**
     * If true this parameter cannot be overridden by parameters of the same
     * name declared in child classes regardless of whether the child class
     * declares the {@link #override()} property to be true
     * 
     * @return True if sealed, false otherwise
     */
    boolean sealed() default false;

    /**
     * If provided restricts the values for the option to the given set of
     * values
     * 
     * @return Allowed values
     */
    String[] allowedValues() default {};

    /**
     * If true the case on {@link #allowedValues()} is ignored
     *
     * @return Ignore case
     */
    boolean ignoreCase() default false;

    /**
     * Sets the desired completion behaviour
     * <p>
     * This is used by usage generators that are capable of generating
     * completion scripts. It indicates any additional completion behaviour
     * that should apply in addition to completions generated from the
     * {@link #allowedValues()} and/or {@link #completionCommand()}. You should
     * use the constants from {@link CompletionBehaviour} as values to this.
     * </p>
     * 
     * @return Completion Behaviours
     */
    int completionBehaviour() default CompletionBehaviour.NONE;

    /**
     * Provides a command used to generate the completion words for this option
     * <p>
     * This is used by usage generators that are capable of generating
     * completion scripts. If you instead want to limit the completions to a
     * fixed set of strings then you should simply set the
     * {@link #allowedValues()} property appropriately.
     * </p>
     * 
     * @return Completion command
     */
    String completionCommand() default "";
}
