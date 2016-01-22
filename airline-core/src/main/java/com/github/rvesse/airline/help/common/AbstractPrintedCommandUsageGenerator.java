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
package com.github.rvesse.airline.help.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Comparator;

import com.github.rvesse.airline.io.printers.UsagePrinter;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;

/**
 * Abstract command usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedCommandUsageGenerator extends AbstractCommandUsageGenerator {

    private final int columnSize;

    public AbstractPrintedCommandUsageGenerator(int columns, Comparator<? super OptionMetadata> optionComparator,
            boolean includeHidden) {
        super(optionComparator, includeHidden);
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be greater than 0");
        this.columnSize = columns;
    }

    /**
     * Generate the help and output it using the provided {@link UsagePrinter}
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
     *            Usage printer to output with
     * @throws IOException
     */
    public abstract <T> void usage(String programName, String[] groupNames, String commandName,
            CommandMetadata command, ParserMetadata<T> parserConfig, UsagePrinter out) throws IOException;

    /**
     * Creates a usage printer for the given stream
     * 
     * @param out
     *            Output stream
     * @return Usage Printer
     */
    protected UsagePrinter createUsagePrinter(OutputStream out) {
        if (out == null)
            throw new NullPointerException("out cannot be null");
        return new UsagePrinter(new PrintWriter(out), columnSize);
    }

    @Override
    public <T> void usage(String programName, String[] groupNames, String commandName, CommandMetadata command,
            ParserMetadata<T> parserConfig, OutputStream out) throws IOException {
        UsagePrinter printer = createUsagePrinter(out);
        if (parserConfig == null) {
            parserConfig = MetadataLoader.loadParser(command.getType());
        }
        usage(programName, groupNames, commandName, command, parserConfig, printer);
        printer.flush();
    }

}
