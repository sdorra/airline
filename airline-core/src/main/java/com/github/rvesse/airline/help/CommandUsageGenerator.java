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
package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.model.CommandMetadata;

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
     */
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
     * @param out
     *            Stream to output to
     * @throws IOException
     */
    public abstract void usage(String programName, String[] groupNames, String commandName, CommandMetadata command,
            OutputStream output) throws IOException;
}