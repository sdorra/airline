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
package com.github.rvesse.airline.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies a command alias
 * <p>
 * This annotation is used as arguments to the {@link Parser} annotation to
 * specify command aliases which provide shortcuts to other commands.
 * </p>
 * 
 * @author rvesse
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Alias {

    /**
     * Name of the alias
     * <p>
     * If this is the same as the name of a command then this alias may not be
     * honoured unless the parser configuration specifies that aliases override
     * built-in commands.
     * </p>
     * 
     * @return Name
     */
    String name();

    /**
     * Arguments for the alias i.e. how the alias should be expanded
     * <p>
     * These may be constants such as <code>{ "foo", "--bar" }</code> which
     * would cause passing in the alias to be the same as passing in the
     * arguments {@code foo} and {@code --bar}. Alternatively these may include
     * positional parameters which are indicated as {@code $N} where {@code N}
     * is an index starting at 1.
     * </p>
     * <p>
     * Where positional parameters are used then that parameter is expanded to
     * take in the users inputs after the alias name into account. For example
     * imagine an alias defined with arguments
     * <code>{ "foo", "$2", "$1" }</code>, if the user invoked the alias as
     * {@code alias a b} then the alias gets expanded and the actual command
     * invoked is {@code foo b a}. In the case where a positional parameter has
     * no corresponding input provided it is left as-is which will usually
     * result in parser errors.
     * </p>
     * <p>
     * By default aliases may not refer to other aliases though the parser may
     * be configured to allow this if desired. However even when allowed
     * circular references are not permitted and will still result in a parser
     * error.
     * </p>
     * 
     * @return Arguments
     */
    String[]arguments();
}
