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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.types.DefaultTypeConverterProvider;
import com.github.rvesse.airline.types.TypeConverterProvider;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
@Documented
public @interface Arguments {
    /**
     * Name or names of the arguments.
     */
    String[] title() default { "" };

    /**
     * A description of the arguments.
     */
    String description() default "";
    
    /**
     * Sets an alternative type converter provider for the arguments. This allows
     * the type converter for arguments to be customised appropriately. By
     * default this will defer to using the type converter provided in the
     * parser configuration.
     * 
     * @return Type converter provider
     */
    Class<? extends TypeConverterProvider> typeConverterProvider() default DefaultTypeConverterProvider.class;
}
