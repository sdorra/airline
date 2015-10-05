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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class as a command
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Command {
    /**
     * Name of the command
     * 
     * @return Command name
     */
    String name();

    /**
     * Description of the command
     * 
     * @return Command description
     */
    String description() default "";

    /**
     * If true, this command won't appear in help
     * 
     * @return Whether this command is hidden
     */
    boolean hidden() default false;

    /**
     * The group(s) this command should belong to. if left empty the command
     * will belong to the default command group
     * <p>
     * If a group name contains spaces then this is interpreted as referring to
     * a sub-group, for example {@code foo bar} would place this command into
     * the {@code bar} group which would be placed as a sub-group of the
     * {@code foo} group.
     * </p>
     * 
     * @return Command groups
     */
    String[]groupNames() default {};
}
