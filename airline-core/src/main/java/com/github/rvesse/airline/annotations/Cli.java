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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.restrictions.GlobalRestriction;

/**
 * Class annotation used to declaratively specify a CLI
 * @author rvesse
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Cli {

    public static final class NO_DEFAULT {
    }

    /**
     * Sets the name of the CLI i.e. the command name users enter to invoke your
     * CLI
     * 
     * @return Name
     */
    String name() default "";

    /**
     * Sets the description of the CLI
     * 
     * @return Description
     */
    String description() default "";

    /**
     * Sets the parser configuration for the CLI
     * 
     * @return Parser Configuration
     */
    Parser parserConfiguration() default @Parser()
    ;

    /**
     * Defines command groups for the CLI
     * 
     * @return Command groups
     */
    Group[]groups() default {};

    /**
     * Defines the class that provides the default command for the CLI
     * 
     * @return Default command class
     */
    Class<?>defaultCommand() default NO_DEFAULT.class;

    /**
     * Defines the classes that provide top-level commands for the CLI
     * 
     * @return Top-level command classes
     */
    Class<?>[]commands() default {};

    /**
     * Defines the classes that provide global restrictions for the CLI
     * 
     * @return Global restriction classes
     */
    Class<? extends GlobalRestriction>[]restrictions() default {};

    /**
     * Sets whether the default global restrictions are applied
     * 
     * @return True if default restrictions are applied, false otherwise
     */
    boolean includeDefaultRestrictions() default true;
}
