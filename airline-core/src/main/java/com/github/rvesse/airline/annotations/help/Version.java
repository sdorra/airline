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
package com.github.rvesse.airline.annotations.help;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface Version {
    /**
     * Sets paths to resources that provide the version information
     * <p>
     * When this annotation is converted into a help section these are the files
     * that are scanned for the version information used to produce the Version
     * help section. Paths are scanned for both on the classpath and on the
     * local file system, if a path identifies both then the classpath source is
     * used and the file system source is ignored. If you want to force a file
     * system path to be used then you can prepend the path with {@code file://}
     * </p>
     * <p>
     * Each source will be provided as a separate list/table row within the help
     * section depending on the setting of the {@link #tabular()} field.
     * </p>
     * 
     * @return
     */
    String[] sources();

    String componentProperty() default "component";

    String versionProperty() default "version";

    String buildProperty() default "build";

    String dateProperty() default "buildDate";

    String[] additionalProperties() default {};

    String[] additionalTitles() default {};

    /**
     * Sets whether to suppress this help section if there is an error obtaining
     * the version information.
     * <p>
     * When set to {@code false} a runtime error will be thrown if this
     * annotation is used and the version information cannot be successfully
     * obtained. When set to {@code true} then if the version information cannot
     * be obtained the help section will either be suppressed if no information
     * was found or if some information was found then partial information is
     * shown.
     * </p>
     * 
     * @return True if errors in obtaining version information are suppressed
     */
    boolean suppressOnError();

    /**
     * Whether to display the version information in a tabular format
     * 
     * @return True if tabular format, false for list format
     */
    boolean tabular() default false;
}
