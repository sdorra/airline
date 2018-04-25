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

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class as providing command group metadata
 */
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
@Documented
public @interface Group {
    public static final class NO_DEFAULT {
    }

    /**
     * Name of the group.
     * <p>
     * If the name contains spaces then this is interpreted as a sub-group, for
     * example {@code foo bar} would be interpreted as a group {@code foo} with
     * a sub-group {@code bar}. All the other fields on this annotation are
     * interpreted as applying to the sub-group being specified.
     * <p>
     * If you also wished to place commands into the {@code foo} group you would
     * need to specify this separately. Since only one instance of an annotation
     * can appear on any given class you would need to use the {@link Groups}
     * annotation to place multiple {@link Group} annotations on a class.
     * </p>
     * 
     * @return Name
     */
    String name();

    /**
     * Description of the group.
     * 
     * @return Description
     */
    String description() default "";

    /**
     * Default command class for the group (optional)
     * 
     * @return Default Command for the group
     */
    Class<?> defaultCommand() default NO_DEFAULT.class;

    /**
     * Command classes to add to the group (optional)
     * 
     * @return Command classes for the group
     */
    Class<?>[] commands() default {};

    /**
     * Whether the group should be hidden
     * 
     * @return True if hidden, false otherwise
     */
    boolean hidden() default false;
}
