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
package com.github.rvesse.airline.help.markdown;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.common.AbstractPrintedCommandUsageGenerator;
import com.github.rvesse.airline.help.common.AbstractUsageGenerator;
import com.github.rvesse.airline.help.markdown.MarkdownCommandUsageGenerator;
import com.github.rvesse.airline.help.markdown.MarkdownGlobalUsageGenerator;
import com.github.rvesse.airline.io.printers.UsagePrinter;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.utils.AirlineUtils;

public class MarkdownMultiPageGlobalUsageGenerator<T> extends MarkdownGlobalUsageGenerator<T> {
    
    private File baseDirectory;
    
    public MarkdownMultiPageGlobalUsageGenerator() {
        this(AbstractUsageGenerator.DEFAULT_COLUMNS, false, new MarkdownCommandUsageGenerator(false), null);
    }
    
    public MarkdownMultiPageGlobalUsageGenerator(boolean includeHidden) {
        this(AbstractUsageGenerator.DEFAULT_COLUMNS, includeHidden, new MarkdownCommandUsageGenerator(includeHidden), null);
    }

    public MarkdownMultiPageGlobalUsageGenerator(int columns, boolean includeHidden) {
        this(columns, includeHidden, new MarkdownCommandUsageGenerator(columns, includeHidden), null);
    }
    
    public MarkdownMultiPageGlobalUsageGenerator(int columns, boolean includeHidden, File baseDirectory) {
        this(columns, includeHidden, new MarkdownCommandUsageGenerator(columns, includeHidden), baseDirectory);
    }

    protected MarkdownMultiPageGlobalUsageGenerator(int columns, boolean includeHidden,
            AbstractPrintedCommandUsageGenerator commandUsageGenerator, File baseDirectory) {
        super(columns, UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, includeHidden, commandUsageGenerator);
        this.baseDirectory = baseDirectory;
    }

    @Override
    protected void outputGroupCommandUsages(UsagePrinter printer, GlobalMetadata<T> global,
            List<CommandGroupMetadata> groups) throws IOException {
        CommandGroupMetadata group = groups.get(groups.size() - 1);

        for (CommandMetadata command : sortCommands(group.getCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            // Create new separate output stream and writer for each command
            OutputStream output = createCommandFile(global, UsageHelper.toGroupNames(groups), command);

            commandUsageGenerator.usage(global.getName(), UsageHelper.toGroupNames(groups), command.getName(), command,
                    global.getParserConfiguration(), output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(output, global);

            // Flush and close the newly created file
            printer.flush();
            output.flush();
            output.close();
        }

        // Sub-groups
        for (CommandGroupMetadata subGroup : sortCommandGroups(group.getSubGroups())) {
            if (subGroup.isHidden() && !this.includeHidden())
                continue;

            List<CommandGroupMetadata> subGroupPath = AirlineUtils.listCopy(groups);
            subGroupPath.add(subGroup);
            outputGroupCommandUsages(printer, global, subGroupPath);
        }
    }

    protected void outputReferenceToSuite(OutputStream output, GlobalMetadata<T> global) throws IOException {
        UsagePrinter printer = new UsagePrinter(new PrintWriter(output), DEFAULT_COLUMNS);
        
        printer.append("#").append(global.getName()).newline();
        printer.append("Part of the");
        printer.append(String.format("`%s`", global.getName()));
        printer.append("suite").newline();
    }

    protected FileOutputStream createCommandFile(GlobalMetadata<T> global, String[] groupNames, CommandMetadata command)
            throws FileNotFoundException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(getCommandName(global, groupNames, command));
        fileName.append(".md");
        
        File f = this.baseDirectory != null ? new File(this.baseDirectory, fileName.toString()) : new File(fileName.toString());
        return new FileOutputStream(f);
    }

    @Override
    protected void outputDefaultGroupCommandUsages(UsagePrinter printer, GlobalMetadata<T> global)
            throws IOException {
        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            // Create new separate output stream and writer for each command
            OutputStream output = createCommandFile(global, null, command);

            commandUsageGenerator.usage(global.getName(), null, command.getName(), command,
                    global.getParserConfiguration(), output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(output, global);

            // Flush and close the newly created file
            printer.flush();
            output.flush();
            output.close();
        }
    }
    
    protected String getCommandName(GlobalMetadata<T> global, String[] groupNames, CommandMetadata command) {
        // Use full man page reference style since we're going to generate
        // individual man-pages for the commands so the overview man page needs
        // to refer to them properly
        StringBuilder name = new StringBuilder();
        name.append(global.getName()).append("-");
        if (groupNames != null) {
            for (int i = 0; i < groupNames.length; i++) {
                name.append(groupNames[i]).append("-");
            }
        }
        name.append(command.getName());
        return name.toString();
    }
}
