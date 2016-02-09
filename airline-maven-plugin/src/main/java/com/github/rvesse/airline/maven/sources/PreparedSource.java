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
package com.github.rvesse.airline.maven.sources;

import com.github.rvesse.airline.maven.OutputMode;
import com.github.rvesse.airline.maven.RawFormatOptions;
import com.github.rvesse.airline.maven.formats.FormatOptions;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.ParserMetadata;

public class PreparedSource {

    private final Class<?> cls;
    private final GlobalMetadata<Object> global;
    private final CommandMetadata command;
    private final ParserMetadata<Object> parser;
    private final RawFormatOptions rawOptions;
    private final OutputMode outputMode;

    public PreparedSource(Class<?> cls, GlobalMetadata<Object> global, CommandMetadata command,
            RawFormatOptions rawOptions, OutputMode outputMode) {
        this.cls = cls;
        this.global = global;
        this.command = command;
        this.parser = this.global != null ? this.global.getParserConfiguration() : MetadataLoader.loadParser(this.cls);
        this.rawOptions = rawOptions;
        this.outputMode = outputMode;
    }

    public Class<?> getSourceClass() {
        return this.cls;
    }

    public boolean isGlobal() {
        return this.global != null;
    }

    public boolean isCommand() {
        return this.command != null;
    }

    public CommandMetadata getCommmand() {
        return this.command;
    }

    public GlobalMetadata<Object> getGlobal() {
        return this.global;
    }

    public ParserMetadata<Object> getParserConfiguration() {
        return this.parser;
    }

    public FormatOptions getFormatOptions(FormatOptions defaultOptions) {
        if (this.rawOptions == null)
            return defaultOptions;
        return new FormatOptions(this.rawOptions, defaultOptions);
    }

    public OutputMode getOutputMode() {
        return this.outputMode;
    }

    public boolean shouldOutputCommandHelp() {
        return this.outputMode == OutputMode.DEFAULT || this.outputMode == OutputMode.COMMAND;
    }

    public boolean shouldOutputGlobalHelp() {
        return this.outputMode == OutputMode.DEFAULT || this.outputMode == OutputMode.CLI;
    }
}
