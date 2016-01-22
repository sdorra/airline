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

package com.github.rvesse.airline.annotations.restrictions.global;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.annotations.Arguments;

/**
 * Marks that a CLI does not permit any command line arguments that aren't
 * recognised as either options or arguments.
 * <p>
 * Note that if the specified command
 * has an {@link Arguments} annotated field then this restriction has no effect
 * because any unrecognised command line arguments are placed into the
 * {@linkplain Arguments} annotated field.
 * </p>
 * 
 * @author rvesse
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface NoUnexpectedArguments {

}
