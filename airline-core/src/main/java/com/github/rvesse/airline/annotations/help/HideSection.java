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

package com.github.rvesse.airline.annotations.help;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation used to specify that a section that would otherwise be inherited
 * from a parent class should be hidden
 * <p>
 * Annotations for help sections are automatically inherited down the class
 * hierarchy which allows for a parent class to define a common help section
 * (e.g. {@link ExitCodes}). This can be overridden by specifying it again
 * further down the class hierarchy but in some cases it may actually be
 * desirable to hide an inherited section entirely in which case this annotation
 * may be used.
 * </p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface HideSection {

    /**
     * Sets the title of the section to hide
     * 
     * @return Section title
     */
    String title();
}
