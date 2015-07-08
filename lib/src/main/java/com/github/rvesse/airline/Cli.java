/**
 * Copyright (C) 2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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

package com.github.rvesse.airline;

import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.parser.command.CliParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Cli<C> {
    /**
     * Creates a builder for specifying a command line in fluent style
     * 
     * @param name
     *            Program name
     * @return CLI Builder
     */
    public static <T> CliBuilder<T> builder(String name) {
        Preconditions.checkNotNull(name, "name is null");
        return new CliBuilder<T>(name);
    }

    private final GlobalMetadata<C> metadata;

    /**
     * Creates a new CLI
     * 
     * @param metadata
     *            Metadata
     */
    public Cli(GlobalMetadata<C> metadata) {
        Preconditions.checkNotNull(metadata);
        this.metadata = metadata;
    }

    public GlobalMetadata<C> getMetadata() {
        return metadata;
    }

    public C parse(String... args) {
        return parse(ImmutableList.copyOf(args));
    }

    private C parse(Iterable<String> args) {
        CliParser<C> parser = new CliParser<C>();
        return parser.parse(metadata, args);
    }
}
