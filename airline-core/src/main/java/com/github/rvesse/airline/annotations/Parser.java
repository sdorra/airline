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

import com.github.rvesse.airline.CommandFactory;
import com.github.rvesse.airline.DefaultCommandFactory;
import com.github.rvesse.airline.DefaultTypeConverter;
import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.options.OptionParser;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Parser {

    /**
     * Controls whether command names may be abbreviated (provided such
     * abbreviations are unambiguous)
     * 
     * @return True if command abbreviation allowed, false otherwise
     */
    boolean allowCommandAbbreviation() default false;

    /**
     * Controls whether options names may be abbreviated (provided such
     * abbreviations are unambiguous)
     * 
     * @return True if option abbreviation allowed, false otherwise
     */
    boolean allowOptionAbbreviation() default false;

    /**
     * Controls the separator that is used to distinguish options from arguments
     * were arguments may be confused as options
     * <p>
     * The default is {@code --} which is the widely used convention
     * </p>
     * 
     * @return Arguments separator
     */
    String argumentsSeparator() default ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR;

    /**
     * Controls whether command alises may be chained i.e. can aliases be
     * defined in terms of other aliases
     * <p>
     * Note that even when enabled circular references are not permitted
     * </p>
     * 
     * @return True if aliases may be chained, false otherwise
     */
    boolean aliasesMayChain() default false;

    /**
     * Controls whether aliases are allowed to override built-in commands i.e.
     * if a command and an alias are defined with the same name does the alias
     * take precedence
     * <p>
     * This is particularly important if you allow users to define aliases since
     * allowing overriding would allow them to change the behaviour from the
     * default expected
     * </p>
     * 
     * @return True if aliases may override built-ins, false otherwise
     */
    boolean aliasesOverrideBuiltIns() default false;

    /**
     * Defines command aliases
     * 
     * @return Command aliases
     */
    Alias[]aliases() default {};

    /**
     * Defines the name of a file from which user defined command aliases should
     * be read
     * 
     * @return User aliases filename
     */
    String userAliasesFile() default "";

    /**
     * Defines the search locations (i.e. directories) where the properties file
     * containing the user defined aliases may exist
     * <p>
     * These should be given in order of preference, properties from all
     * locations will be merged together such that properties from the locations
     * earlier in this list take precedence
     * </p>
     * <p>
     * Search locations may start with {@code ~/} or {@code ~\} (depending on
     * the target platform) to refer to the home directory
     * </p>
     * 
     * @return
     */
    String[]userAliasesSearchLocation() default "";

    /**
     * Sets the prefix used for properties that define aliases
     * <p>
     * This is useful if you use the same properties file to store general
     * properties for your application in the same properties file. If set only
     * properties whose names begin with these prefix are treated as alias
     * definition with the prefix being stripped. So for example if you have a
     * prefix of {@code foo.} and defined a property {@code foo.bar} then you
     * would be defining an alias {@code bar}
     * </p>
     * 
     * @return User defined aliases prefix
     */
    String userAliasesPrefix() default "";

    /**
     * Sets whether to use the default set of option parsers
     * 
     * @return True if default option parsers are used, false otherwise
     */
    boolean useDefaultOptionParsers() default true;

    /**
     * Sets whether to use the default option parsers first before any
     * additional option parsers that may be defined
     * 
     * @return
     */
    boolean defaultParsersFirst() default true;

    /**
     * Sets the option parser classes to be used
     * 
     * @return Option parser classes
     */
    @SuppressWarnings("rawtypes")
    Class<? extends OptionParser>[]optionParsers() default {};

    /**
     * Sets the command factory class to use
     * 
     * @return Command factory class
     */
    @SuppressWarnings("rawtypes")
    Class<? extends CommandFactory>commandFactory() default DefaultCommandFactory.class;

    /**
     * Sets the type converter class to use
     * 
     * @return Type converter class
     */
    Class<? extends TypeConverter>typeConverter() default DefaultTypeConverter.class;
}
