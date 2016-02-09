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
package com.github.rvesse.airline.maven.formats;

import java.util.Properties;

import com.github.rvesse.airline.help.common.AbstractUsageGenerator;
import com.github.rvesse.airline.help.man.ManSections;
import com.github.rvesse.airline.maven.RawFormatOptions;

public class FormatOptions {
    private final Integer columns, manSection;
    private final Boolean includeHidden, multiPage;
    private final Properties properties;
    private final FormatOptions parent;
    
    public FormatOptions() {
        this(new RawFormatOptions(), null);
    }
    
    public FormatOptions(RawFormatOptions options) {
        this(options, null);
    }
    
    public FormatOptions(RawFormatOptions options, FormatOptions parentOptions) {
        this.parent = parentOptions;
        this.columns = options.columns;
        this.manSection = options.manSection;
        this.includeHidden = options.includeHidden;
        this.multiPage = options.includeHidden;
        this.properties = options.properties;
    }
    
    public int getColumns() {
        return this.columns != null ? this.columns.intValue() : this.parent != null ? this.parent.getColumns() : AbstractUsageGenerator.DEFAULT_COLUMNS;
    }
    
    public int getManSection() {
        return this.manSection != null ? this.manSection.intValue() : this.parent != null ? this.parent.getManSection() : ManSections.GENERAL_COMMANDS;
    }
    
    public boolean includeHidden() {
        return this.includeHidden != null ? this.includeHidden.booleanValue() : this.parent != null ? this.parent.includeHidden() : false;
    }
    
    public boolean useMultiPage() {
        return this.multiPage != null ? this.multiPage.booleanValue() : this.parent != null ? this.parent.useMultiPage() : false;
    }
    
    public Object get(Object key) {
        Object value = this.properties.get(key);
        if (value == null && this.parent != null) {
            value = this.parent.get(key);
        }
        return value;
    }
    
    public String getProperty(String key) {
        String value = this.properties.getProperty(key);
        if (value == null && this.parent != null) {
            value = this.parent.getProperty(key);
        }
        return value;
    }
}
