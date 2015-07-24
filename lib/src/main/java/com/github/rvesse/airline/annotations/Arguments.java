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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.CompletionBehaviour;

import static java.lang.annotation.ElementType.FIELD;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
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
     */
    String usage() default "";

    /**
     * Whether these arguments are required.
     */
    boolean required() default false;

    /**
     * The arity of the arguments, a value of less than or equal to zero is
     * treated as unlimited
     */
    int arity() default Integer.MIN_VALUE;

    /**
     * Sets the desired completion behaviour
     * <p>
     * This is used by usage generators that are capable of generating
     * completion scripts. It indicates any additional completion behaviour that
     * should apply in addition to completions generated from the
     * {@link #completionCommand()} if set. You should use the constants from
     * {@link CompletionBehaviour} as values to this.
     * </p>
     * 
     * @return Completion Behaviours
     */
    int completionBehaviour() default CompletionBehaviour.NONE;

    /**
     * Provides a command used to generate the completion words for the
     * arguments
     * <p>
     * This is used by usage generators that are capable of generating
     * completion scripts. You can also use the {@link #completionBehaviour()}
     * property if you want to use system supplied completions
     * </p>
     * 
     * @return Completion command
     */
    String completionCommand() default "";
}
