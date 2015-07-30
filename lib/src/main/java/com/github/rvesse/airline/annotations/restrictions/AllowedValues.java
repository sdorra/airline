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
package com.github.rvesse.airline.annotations.restrictions;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;

/**
 * Annotation that marks that the option/arguments are restricted to a given set
 * of values
 * <p>
 * Unlike {@link AllowedRawValues} this restriction works against the values
 * after they have been converted into Java objects and thus can provide more
 * accurate restriction than {@link AllowedValues} can provide. The trade off is
 * that enforcing this restriction is marginally more complex because it
 * requires parsing the allowed values.
 * </p>
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface AllowedValues {

    /**
     * If provided restricts the values for the option to the given set of
     * values
     * 
     * @return Allowed values
     */
    String[] allowedValues() default {};
}
