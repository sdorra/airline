package io.airlift.airline.help.ronn;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import io.airlift.airline.help.CommandUsageGenerator;
import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;

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
 * 
 */
public class RonnMultiPageGlobalUsageGenerator extends RonnGlobalUsageGenerator {

    public RonnMultiPageGlobalUsageGenerator() {
        this(ManSections.GENERAL_COMMANDS, new RonnCommandUsageGenerator(ManSections.GENERAL_COMMANDS, false, true));
    }

    public RonnMultiPageGlobalUsageGenerator(int manSection) {
        this(manSection, new RonnCommandUsageGenerator(manSection, false, true));
    }

    public RonnMultiPageGlobalUsageGenerator(int manSection, boolean includeHidden) {
        this(manSection, new RonnCommandUsageGenerator(manSection, includeHidden, true));
    }

    protected RonnMultiPageGlobalUsageGenerator(int manSection, CommandUsageGenerator commandUsageGenerator) {
        super(manSection, commandUsageGenerator);
    }

    @Override
    protected String getCommandName(GlobalMetadata global, String groupName, CommandMetadata command) {
        // Use full man page reference style since we're going to generate
        // individual man-pages for the commands so the overview man page needs
        // to refer to them properly
        StringBuilder name = new StringBuilder();
        name.append(global.getName()).append("-");
        if (groupName != null) {
            name.append(groupName).append("-");
        }
        name.append(command.getName());
        name.append("(").append(Integer.toString(this.manSection)).append(")");
        return name.toString();
    }

    @Override
    protected void outputCommandUsages(OutputStream output, Writer writer, GlobalMetadata global) throws IOException {
        // Default group usages
        outputDefaultGroupCommandUsages(output, writer, global);

        // Other group usages
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            outputGroupCommandUsages(output, writer, global, group);
        }
    }

    @Override
    protected void outputGroupCommandUsages(OutputStream output, Writer writer, GlobalMetadata global,
            CommandGroupMetadata group) throws IOException {

        for (CommandMetadata command : sortCommands(group.getCommands())) {
            if (command.isHidden())
                continue;

            // Create new separate output stream and writer for each command
            output = createCommandFile(global, group.getName(), command);
            writer = new OutputStreamWriter(output);

            commandUsageGenerator.usage(global.getName(), group.getName(), command.getName(), command, output);

            // Write a reference back to the suite man page
            outputReferenceToSuite(global, writer);

            // Flush and close the newly created file
            writer.flush();
            output.flush();
            writer.close();
            output.close();
        }
    }

    protected void outputReferenceToSuite(GlobalMetadata global, Writer writer) throws IOException {
        writer.append(NEW_PARA).append("## ").append(global.getName().toUpperCase()).append(NEW_PARA);
        writer.append("Part of the `").append(global.getName()).append("(").append(Integer.toString(this.manSection))
                .append(")` suite");
    }

    protected FileOutputStream createCommandFile(GlobalMetadata global, String groupName, CommandMetadata command)
            throws FileNotFoundException {
        return new FileOutputStream(getCommandName(global, groupName, command).replace(
                String.format("(%d)", this.manSection), String.format(".%d.ronn", this.manSection)));
    }

    @Override
    protected void outputDefaultGroupCommandUsages(OutputStream output, Writer writer, GlobalMetadata global)
            throws IOException {
        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            if (command.isHidden())
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
