package io.airlift.airline.help.ronn;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import io.airlift.airline.help.AbstractGlobalUsageGenerator;
import io.airlift.airline.help.CommandUsageGenerator;
import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

/**
 * A global usage generator which generates help in <a
 * href="http://rtomayko.github.io/ronn/">Ronn format</a> which can then be
 * transformed into man pages or HTML pages as desired using the Ronn tooling
 * 
 */
public class RonnGlobalUsageGenerator extends AbstractGlobalUsageGenerator {

    protected final CommandUsageGenerator commandUsageGenerator;
    protected final int manSection;
    private static final String NEW_PARA = "\n\n";
    private static final String HORIZONTAL_RULE = "---";

    public RonnGlobalUsageGenerator() {
        this(ManSections.GENERAL_COMMANDS, new RonnCommandUsageGenerator(ManSections.GENERAL_COMMANDS, false));
    }

    public RonnGlobalUsageGenerator(int manSection) {
        this(manSection, new RonnCommandUsageGenerator(manSection, false));
    }

    protected RonnGlobalUsageGenerator(int manSection, CommandUsageGenerator commandUsageGenerator) {
        this.commandUsageGenerator = commandUsageGenerator;
        this.manSection = manSection;
    }

    @Override
    public void usage(GlobalMetadata global, OutputStream output) throws IOException {
        Writer writer = new OutputStreamWriter(output);

        writer.append(global.getName()).append("(").append(Integer.toString(this.manSection)).append(") -- ");
        writer.append(global.getDescription()).append("\n");
        writer.append("==========");

        writer.append(NEW_PARA).append("## SYNOPSIS").append(NEW_PARA);
        List<OptionMetadata> options = newArrayList();
        List<OptionMetadata> aOptions;
        writer.append("`").append(global.getName()).append("`");
        aOptions = global.getOptions();
        if (aOptions != null && aOptions.size() > 0) {
            writer.append(" ").append(Joiner.on(" ").join(toSynopsisUsage(sortOptions(aOptions))));
            options.addAll(aOptions);
        }
        if (global.getCommandGroups().size() > 0) {
            writer.append(" [<group>] <command> [command-args]");
        } else {
            writer.append(" <command> [command-args]");
        }

        if (options.size() > 0) {
            writer.append(NEW_PARA).append("## GLOBAL OPTIONS");
            options = sortOptions(options);

            for (OptionMetadata option : options) {
                // skip hidden options
                if (option.isHidden()) {
                    continue;
                }

                // option names
                writer.append(NEW_PARA).append("* ").append(toDescription(option)).append(":\n");

                // description
                writer.append(option.getDescription());
            }
        }

        // TODO If we add Discussion and Examples to global meta-data reinstate
        // this
        //@formatter:off
//        if (global.getDiscussion() != null) {
//            writer.append(NEW_PARA).append("## DISCUSSION").append(NEW_PARA);
//            writer.append(global.getDiscussion());
//        }
//
//        if (global.getExamples() != null && !global.getExamples().isEmpty()) {
//            writer.append(NEW_PARA).append("## EXAMPLES");
//
//            // this will only work for "well-formed" examples
//            for (int i = 0; i < global.getExamples().size(); i += 3) {
//                String aText = global.getExamples().get(i).trim();
//
//                if (aText.startsWith("*")) {
//                    aText = aText.substring(1).trim();
//                }
//
//                writer.append(NEW_PARA).append("* ").append(aText).append(":\n");
//            }
//        }
        //@formatter:on

        writer.flush();
        output.flush();

        if (global.getCommandGroups().size() > 0) {
            // Command Groups
            writer.append(NEW_PARA).append("## COMMAND GROUPS").append(NEW_PARA);
            writer.append("Commands are grouped as follows:");

            if (global.getDefaultGroupCommands().size() > 0) {
                writer.append(NEW_PARA).append("* Default (no <group> specified)");
                for (CommandMetadata command : global.getDefaultGroupCommands()) {
                    if (command.isHidden())
                        continue;

                    writer.append(NEW_PARA).append("  * `").append(getCommandName(global, null, command))
                            .append("`:\n");
                    writer.append("  ").append(command.getDescription());
                }
            }

            for (CommandGroupMetadata group : global.getCommandGroups()) {
                writer.append(NEW_PARA).append("* **").append(group.getName()).append("**").append(NEW_PARA);
                writer.append("  ").append(group.getDescription());

                for (CommandMetadata command : group.getCommands()) {
                    if (command.isHidden())
                        continue;

                    writer.append(NEW_PARA).append("  * `").append(getCommandName(global, group.getName(), command))
                            .append("`:\n");
                    writer.append("  ").append(command.getDescription());
                }
            }

            outputCommandUsages(global, output, writer);
        } else {
            // No Groups
            writer.append(NEW_PARA).append("## COMMANDS");

            for (CommandMetadata command : global.getDefaultGroupCommands()) {
                if (command.isHidden())
                    continue;

                writer.append(NEW_PARA).append("* `").append(getCommandName(global, null, command)).append("`:\n");
                writer.append(command.getDescription());
            }

            outputCommandUsages(global, output, writer);
        }

        // Flush the output
        writer.flush();
        output.flush();
    }

    /**
     * Outputs the command usages
     * 
     * @param global
     *            Global metadata
     * @param output
     *            Output stream
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void outputCommandUsages(GlobalMetadata global, OutputStream output, Writer writer) throws IOException {
        writer.append(NEW_PARA).append(HORIZONTAL_RULE).append(NEW_PARA);

        // Default group usages
        outputDefaultGroupCommandUsages(global, output, writer);

        // Other group usages
        for (CommandGroupMetadata group : global.getCommandGroups()) {
            outputGroupCommandUsages(global, output, writer, group);
        }
    }

    /**
     * Gets the display name for a command
     * 
     * @param global
     *            Global metadata
     * @param groupName
     *            Group name (may be null)
     * @param command
     *            Command metadata
     * @return Display name for the command
     */
    protected String getCommandName(GlobalMetadata global, String groupName, CommandMetadata command) {
        return command.getName();
    }

    protected void outputGroupCommandUsages(GlobalMetadata global, OutputStream output, Writer writer,
            CommandGroupMetadata group) throws IOException {
        for (CommandMetadata command : group.getCommands()) {
            if (command.isHidden())
                continue;

            writer.flush();
            output.flush();
            commandUsageGenerator.usage(global.getName(), group.getName(), command.getName(), command, output);
            writer.append(NEW_PARA).append(HORIZONTAL_RULE).append(NEW_PARA);
        }
    }

    protected void outputDefaultGroupCommandUsages(GlobalMetadata global, OutputStream output, Writer writer)
            throws IOException {
        for (CommandMetadata command : global.getDefaultGroupCommands()) {
            if (command.isHidden())
                continue;

            writer.flush();
            output.flush();
            commandUsageGenerator.usage(global.getName(), null, command.getName(), command, output);
            writer.append(NEW_PARA).append(HORIZONTAL_RULE).append(NEW_PARA);
        }
    }

    @Override
    protected String toDescription(OptionMetadata option) {
        Set<String> options = option.getOptions();
        StringBuilder stringBuilder = new StringBuilder();

        final String argumentString;
        if (option.getArity() > 0) {
            argumentString = Joiner.on(" ").join(
                    Lists.transform(ImmutableList.of(option.getTitle()), new Function<String, String>() {
                        public String apply(@Nullable String argument) {
                            return "<" + argument + ">";
                        }
                    }));
        } else {
            argumentString = null;
        }

        Joiner.on(", ").appendTo(stringBuilder, transform(options, new Function<String, String>() {
            public String apply(@Nullable String option) {
                if (argumentString != null) {
                    return "`" + option + "` " + argumentString;
                }
                return "`" + option + "`";
            }
        }));

        return stringBuilder.toString();
    }
}
