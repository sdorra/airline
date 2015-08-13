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

    boolean allowCommandAbbreviation() default false;
    
    boolean allowOptionAbbreviation() default false;
    
    String argumentsSeparator() default ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR;
    
    boolean aliasesMayChain() default false;
    
    boolean aliasesOverrideBuiltIns() default false;
    
    Alias[] aliases() default {};
    
    String userAliasesFile() default "";
    
    String[] userAliasesSearchLocation() default "";
    
    String userAliasesPrefix() default "";
    
    boolean useDefaultOptionParsers() default true;
    
    boolean defaultParsersFirst() default true;
    
    @SuppressWarnings("rawtypes")
    Class<? extends OptionParser>[] optionParsers() default {};
    
    @SuppressWarnings("rawtypes")
    Class<? extends CommandFactory> commandFactory() default DefaultCommandFactory.class;
    
    Class<? extends TypeConverter> typeConverter() default DefaultTypeConverter.class;
}
