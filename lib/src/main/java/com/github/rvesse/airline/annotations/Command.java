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
     * <p>
     * Command name is split on white space to form a multi-word name
     * </p>
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
     * An array of lines of text to provide a series of example usages of the
     * command e.g.
     * 
     * <pre>
     * examples= {&quot;Explain what the command example does&quot;,
     *     &quot;$ cli group cmd foo.txt file.json&quot;,
     *     &quot;&quot;,
     *     &quot;Explain what this command example does&quot;,
     *     &quot;$ cli group cmd --non-standard-option value foo.txt&quot;}
     * </pre>
     * <p>
     * Blank lines are preserved to give users leverage over how the examples
     * are displayed in the usage. However individual help generators may still
     * choose to format the examples as they desire.
     * </p>
     * <p>
     * Example lines are not required to be in any specific order i.e. you can
     * do example then explanation, explanation then example, multi-line
     * explanations or whatever you see fit.
     * </p>
     * <p>
     * If you have more free form discussion about the command it should be
     * placed in the {@link #discussion()} property.
     * </p>
     * 
     * @return Examples
     */
    String[] examples() default {};

    /**
     * An array of lines of text that provides an extended discussion on the
     * behaviour of the command. Should supplement the shorter description which
     * is more of a summary where discussion can get into greater detail about
     * exact behaviour of commands.
     * 
     * @return Command discussion
     */
    String[] discussion() default {};

    /**
     * The group(s) this command should belong to. if left empty the command
     * will belong to the default command group
     * 
     * @return Command groups
     */
    String[] groupNames() default {};

    /**
     * The exit codes that this command may return, meanings of the exit codes
     * may be given using the {@link #exitDescriptions()} property. The data in
     * these two properties is collated based on array indices.
     * 
     * @return Array of exit codes
     */
    int[] exitCodes() default {};

    /**
     * Descriptions of the meanings of the exit codes this command may return,
     * the exit codes are given by the {@link #exitCodes()} property. The data
     * in these two properties is collated based on array indices.
     * 
     * @return
     */
    String[] exitDescriptions() default {};
}
