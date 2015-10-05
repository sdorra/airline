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
package com.github.rvesse.airline.annotations.restrictions.ranges;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that marks values as being restricted to a given lexical range
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface LexicalRange {

    /**
     * Minimum value, the empty string is interpreted as no minimum
     * 
     * @return Minimum value
     */
    String min() default "";

    /**
     * Maximum value, the empty string is interpreted as no maximum
     * 
     * @return Maximum value
     */
    String max() default "";

    /**
     * Whether the minimum is inclusive
     * 
     * @return True if inclusive, false if exclusive
     */
    boolean minInclusive() default true;

    /**
     * Whether the maximum is inclusive
     * 
     * @return True if inclusive, false if exclusive
     */
    boolean maxInclusive() default true;

    /**
     * The locale used for comparisons
     * 
     * @return Locale BCP47 tag
     */
    String locale() default "en";
}
