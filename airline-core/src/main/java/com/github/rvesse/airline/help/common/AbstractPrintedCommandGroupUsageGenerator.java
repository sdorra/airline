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

import com.github.rvesse.airline.help.sections.HelpHint;
import com.github.rvesse.airline.io.printers.UsagePrinter;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * Abstract command group usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedCommandGroupUsageGenerator<T> extends AbstractCommandGroupUsageGenerator<T> {

    private final int columnSize;

    public AbstractPrintedCommandGroupUsageGenerator(int columnSize, Comparator<? super HelpHint> hintComparator, 
            Comparator<? super OptionMetadata> optionComparator, Comparator<? super CommandMetadata> commandComparator,
            boolean includeHidden) {
        super(hintComparator, optionComparator, commandComparator, includeHidden);
        if (columnSize <= 0)
            throw new IllegalArgumentException("columnSize must be greater than 0");
        this.columnSize = columnSize;
    }

    /**
     * Generate the help and output is using the provided {@link UsagePrinter}
     * 
     * @param global
     *            Global Metadata
     * @param group
     *            Group Metadata
     * @param out
     *            Usage printer to output with
     * @throws IOException
     */
    protected abstract void usage(GlobalMetadata<T> global, CommandGroupMetadata[] groups, UsagePrinter out)
            throws IOException;

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
    public void usage(GlobalMetadata<T> global, CommandGroupMetadata[] groups, OutputStream out) throws IOException {
        UsagePrinter printer = createUsagePrinter(out);
        usage(global, groups, printer);
        printer.flush();
    }

}
