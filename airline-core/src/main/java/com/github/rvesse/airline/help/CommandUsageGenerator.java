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
package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.ParserMetadata;

/**
 * Interface implemented by classes that can generate usage documentation for a
 * command
 */
public interface CommandUsageGenerator {

    /**
     * Generate the help and output it on standard out
     * 
     * @param programName
     *            Program Name
     * @param groupNames
     *            Group Name(s)
     * @param commandName
     *            Command Name
     * @param command
     *            Command Metadata
     * @throws IOException
     *             Thrown if there is a problem generating usage output
     * @deprecated Please use the overload that takes the parser configuration
     *             explicitly
     */
    @Deprecated
    public abstract void usage(String programName, String[] groupNames, String commandName, CommandMetadata command)
            throws IOException;

    /**
     * Generate the help and output it to the stream
     * 
     * @param programName
     *            Program Name
     * @param groupNames
     *            Group Name(s)
     * @param commandName
     *            Command Name
     * @param command
     *            Command Metadata
     * @param output
     *            Stream to output to
     * @throws IOException
     *             Thrown if there is a problem generating usage output
     * @deprecated Please use the overload that takes the parser configuration
     *             explicitly
     */
    @Deprecated
    public abstract void usage(String programName, String[] groupNames, String commandName, CommandMetadata command,
            OutputStream output) throws IOException;

    /**
     * Generate the help and output it on standard out
     * 
     * @param programName
     *            Program Name
     * @param groupNames
     *            Group Name(s)
     * @param commandName
     *            Command Name
     * @param command
     *            Command Metadata
     * @param parserConfig
     *            Parser configuration, if {@code null} is passed then the
     *            parser configuration is automatically determined based on the
     *            command class for which we are producing help
     * @param <T>
     *            Command type
     * @throws IOException
     *             Thrown if there is a problem generating usage output
     */
    public abstract <T> void usage(String programName, String[] groupNames, String commandName, CommandMetadata command,
            ParserMetadata<T> parserConfig) throws IOException;

    /**
     * Generate the help and output it to the stream
     * 
     * @param programName
     *            Program Name
     * @param groupNames
     *            Group Name(s)
     * @param commandName
     *            Command Name
     * @param command
     *            Command Metadata
     * @param parserConfig
     *            Parser Configuration, if {@code null} is passed then the
     *            parser configuration is automatically determined based on the
     *            command class for which we are producing help
     * @param output
     *            Stream to output to
     * @param <T>
     *            Command type
     * @throws IOException
     *             Thrown if there is a problem generating usage output
     */
    public abstract <T> void usage(String programName, String[] groupNames, String commandName, CommandMetadata command,
            ParserMetadata<T> parserConfig, OutputStream output) throws IOException;
}