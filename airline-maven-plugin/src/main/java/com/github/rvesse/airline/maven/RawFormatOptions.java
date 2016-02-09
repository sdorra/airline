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
package com.github.rvesse.airline.maven;

import java.util.Properties;

import org.apache.maven.plugins.annotations.Parameter;

public class RawFormatOptions {

    /**
     * Indicates the number of columns to use for help that enforces wrapping on
     * the output
     */
    @Parameter(defaultValue = "79")
    public Integer columns;

    /**
     * Indicates whether to include hidden groups, commands and options in
     * generated help
     */
    @Parameter(defaultValue = "false")
    public Boolean includeHidden;

    /**
     * Indicates what section of the manual that Man based help should be
     * generated to
     */
    @Parameter(defaultValue = "1")
    public Integer manSection;

    /**
     * Indicates whether help should be generated to multiple files where
     * possible
     */
    @Parameter(defaultValue = "false")
    public Boolean multiFile;

    /**
     * Provides additional properties which custom formats may use
     */
    @Parameter
    public Properties properties;
}
