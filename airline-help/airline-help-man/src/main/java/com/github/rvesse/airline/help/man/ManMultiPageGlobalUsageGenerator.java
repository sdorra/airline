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
package com.github.rvesse.airline.help.man;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.io.printers.TroffPrinter;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.utils.AirlineUtils;

public class ManMultiPageGlobalUsageGenerator<T> extends ManGlobalUsageGenerator<T> {

    public ManMultiPageGlobalUsageGenerator() {
        this(ManSections.GENERAL_COMMANDS, false, new ManCommandUsageGenerator(ManSections.GENERAL_COMMANDS, false));
    }

    public ManMultiPageGlobalUsageGenerator(int manSection) {
        this(manSection, false, new ManCommandUsageGenerator(manSection, false));
    }

    public ManMultiPageGlobalUsageGenerator(int manSection, boolean includeHidden) {
        this(manSection, includeHidden, new ManCommandUsageGenerator(manSection, includeHidden));
    }

    protected ManMultiPageGlobalUsageGenerator(int manSection, boolean includeHidden,
            CommandUsageGenerator commandUsageGenerator) {
        super(manSection, includeHidden, commandUsageGenerator);
    }

    @Override
    protected void outputCommandUsages(OutputStream output, TroffPrinter printer, GlobalMetadata<T> global)
            throws IOException {
        // Default group usages
        outputDefaultGroupCommandUsages(output, printer, global);

        // Other group usages
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            if (group.isHidden() && !this.includeHidden())
                continue;

            List<CommandGroupMetadata> groupPath = new ArrayList<CommandGroupMetadata>();
            groupPath.add(group);
            outputGroupCommandUsages(output, printer, global, groupPath);
        }
    }

    @Override
    protected void outputGroupCommandUsages(OutputStream output, TroffPrinter printer, GlobalMetadata<T> global,
            List<CommandGroupMetadata> groups) throws IOException {
        CommandGroupMetadata group = groups.get(groups.size() - 1);

        for (CommandMetadata command : sortCommands(group.getCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            // Create new separate output stream and writer for each command
            output = createCommandFile(global, UsageHelper.toGroupNames(groups), command);

            commandUsageGenerator.usage(global.getName(), UsageHelper.toGroupNames(groups), command.getName(), command,
                    global.getParserConfiguration(), output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(new TroffPrinter(new PrintWriter(output)), global);

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
            outputGroupCommandUsages(output, printer, global, subGroupPath);
        }
    }

    protected void outputReferenceToSuite(TroffPrinter printer, GlobalMetadata<T> global) throws IOException {
        printer.nextSection(global.getName().toUpperCase());
        printer.print("Part of the ");
        printer.printBold(String.format("%s(%d)", global.getName(), this.manSection));
        printer.println(" suite");
    }

    protected FileOutputStream createCommandFile(GlobalMetadata<T> global, String[] groupNames, CommandMetadata command)
            throws FileNotFoundException {
        StringBuilder fileName = new StringBuilder();
        if (global.getName() != null) {
            fileName.append(global.getName());
            fileName.append('-');
        }
        if (groupNames != null) {
            for (String group : groupNames) {
                fileName.append(group);
                fileName.append('-');
            }
        }
        fileName.append(getCommandName(global, groupNames, command));
        fileName.append(".");
        fileName.append(this.manSection);
        return new FileOutputStream(fileName.toString());
    }

    @Override
    protected void outputDefaultGroupCommandUsages(OutputStream output, TroffPrinter printer, GlobalMetadata<T> global)
            throws IOException {
        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            // Create new separate output stream and writer for each command
            output = createCommandFile(global, null, command);

            commandUsageGenerator.usage(global.getName(), null, command.getName(), command,
                    global.getParserConfiguration(), output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(new TroffPrinter(new PrintWriter(output)), global);

            // Flush and close the newly created file
            printer.flush();
            output.flush();
            output.close();
        }
    }
}
