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
package com.github.rvesse.airline.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.model.ParserMetadata;

/**
 * Builds a command alias
 * 
 * @author rvesse
 *
 * @param <C>
 *            Command type
 */
public class AliasBuilder<C> extends AbstractChildBuilder<AliasMetadata, ParserMetadata<C>, ParserBuilder<C>> {
    private String name;
    private final List<String> arguments = new ArrayList<String>();

    /**
     * Creates a new alias builder
     * 
     * @param parserBuilder
     *            Parser builder
     * @param name
     *            Alias name
     */
    public AliasBuilder(ParserBuilder<C> parserBuilder) {
        super(parserBuilder);
    }

    /**
     * Creates a new alias builder
     * 
     * @param parserBuilder
     *            Parser builder
     * @param name
     *            Alias name
     */
    public AliasBuilder(ParserBuilder<C> parserBuilder, String name) {
        this(parserBuilder);
        this.withName(name);
    }

    /**
     * Sets the name for the alias
     * 
     * @param name
     *            Alias name
     * @return Alias builder
     */
    public AliasBuilder<C> withName(String name) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Alias name cannot be null/empty/whitespace");
        this.name = name;
        return this;
    }

    /**
     * Sets an argument for the alias
     * 
     * @param arg
     *            Argument
     * @return Alias builder
     */
    public AliasBuilder<C> withArgument(String arg) {
        if (StringUtils.isEmpty(arg))
            throw new IllegalArgumentException("Alias argument cannot be null");
        arguments.add(arg);
        return this;
    }

    /**
     * Sets arguments for the alias
     * 
     * @param args
     *            Arguments
     * @return Alias builder
     */
    public AliasBuilder<C> withArguments(String... args) {
        for (String arg : args) {
            if (arg == null)
                throw new NullPointerException("Alias argument cannot be null");
            arguments.add(arg);
        }
        return this;
    }

    /**
     * Builds the alias metadata
     */
    public AliasMetadata build() {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Alias name cannot be null/empty/whitespace");
        return new AliasMetadata(name, arguments);
    }
}
