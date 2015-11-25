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
package com.github.rvesse.airline.help.ronn;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.man.ManSections;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * A global usage generator that creates a top level overview man page (a la
 * {@code git}) and generates separate man pages for each command provided by
 * the CLI. The top level man page references the other man pages using proper
 * man page style references that RONN will recognise and handle appropriately.
 * <p>
 * The overview man page will be generated to the provided output stream
 * <strong>BUT</strong> new files are generated in the working directory for the
 * individual command man pages
 * </p>
 * 
 * @author rvesse
 * @deprecated The RONN format has some know bugs and it is recommended to use
 *             classes from the airline-help-man or airline-help-markdown
 *             modules instead of classes from this module
 *
 */
@Deprecated
public class RonnMultiPageGlobalUsageGenerator<T> extends RonnGlobalUsageGenerator<T> {

    public RonnMultiPageGlobalUsageGenerator() {
        this(ManSections.GENERAL_COMMANDS, false,
                new RonnCommandUsageGenerator(ManSections.GENERAL_COMMANDS, false, true));
    }

    public RonnMultiPageGlobalUsageGenerator(int manSection) {
        this(manSection, false, new RonnCommandUsageGenerator(manSection, false, true));
    }

    public RonnMultiPageGlobalUsageGenerator(int manSection, boolean includeHidden) {
        this(manSection, includeHidden, new RonnCommandUsageGenerator(manSection, includeHidden, true));
    }

    protected RonnMultiPageGlobalUsageGenerator(int manSection, boolean includeHidden,
            CommandUsageGenerator commandUsageGenerator) {
        super(manSection, includeHidden, commandUsageGenerator);
    }

    @Override
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
        name.append("(").append(Integer.toString(this.manSection)).append(")");
        return name.toString();
    }

    @Override
    protected void outputCommandUsages(OutputStream output, Writer writer, GlobalMetadata<T> global)
            throws IOException {
        // Default group usages
        outputDefaultGroupCommandUsages(output, writer, global);

        // Other group usages
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            if (group.isHidden() && !this.includeHidden())
                continue;

            List<CommandGroupMetadata> groupPath = new ArrayList<CommandGroupMetadata>();
            groupPath.add(group);
            outputGroupCommandUsages(output, writer, global, groupPath);
        }
    }

    @Override
    protected void outputGroupCommandUsages(OutputStream output, Writer writer, GlobalMetadata<T> global,
            List<CommandGroupMetadata> groups) throws IOException {
        CommandGroupMetadata group = groups.get(groups.size() - 1);

        for (CommandMetadata command : sortCommands(group.getCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            // Create new separate output stream and writer for each command
            output = createCommandFile(global, UsageHelper.toGroupNames(groups), command);
            writer = new OutputStreamWriter(output);

            commandUsageGenerator.usage(global.getName(), UsageHelper.toGroupNames(groups), command.getName(), command,
                    output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(global, writer);

            // Flush and close the newly created file
            writer.flush();
            output.flush();
            writer.close();
            output.close();
        }

        // Sub-groups
        for (CommandGroupMetadata subGroup : sortCommandGroups(group.getSubGroups())) {
            if (subGroup.isHidden() && !this.includeHidden())
                continue;

            List<CommandGroupMetadata> subGroupPath = AirlineUtils.listCopy(groups);
            subGroupPath.add(subGroup);
            outputGroupCommandUsages(output, writer, global, subGroupPath);
        }
    }

    protected void outputReferenceToSuite(GlobalMetadata<T> global, Writer writer) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append("## ").append(global.getName().toUpperCase())
                .append(RonnUsageHelper.NEW_PARA);
        writer.append("Part of the `").append(global.getName()).append("(").append(Integer.toString(this.manSection))
                .append(")` suite");
    }

    protected FileOutputStream createCommandFile(GlobalMetadata<T> global, String[] groupNames, CommandMetadata command)
            throws FileNotFoundException {
        return new FileOutputStream(getCommandName(global, groupNames, command)
                .replace(String.format("(%d)", this.manSection), String.format(".%d.ronn", this.manSection)));
    }

    @Override
    protected void outputDefaultGroupCommandUsages(OutputStream output, Writer writer, GlobalMetadata<T> global)
            throws IOException {
        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            // Create new separate output stream and writer for each command
            output = createCommandFile(global, null, command);
            writer = new OutputStreamWriter(output);

            commandUsageGenerator.usage(global.getName(), null, command.getName(), command, output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(global, writer);

            // Flush and close the newly created file
            writer.flush();
            output.flush();
            writer.close();
            output.close();
        }
    }

}
