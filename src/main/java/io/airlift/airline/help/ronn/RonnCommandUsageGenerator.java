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

import io.airlift.airline.help.AbstractCommandUsageGenerator;
import io.airlift.airline.model.ArgumentsMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

/**
 * A command usage generator which generates help in <a
 * href="http://rtomayko.github.io/ronn/">Ronn format</a> which can then be
 * transformed into man pages or HTML pages as desired using the Ronn tooling
 * 
 */
public class RonnCommandUsageGenerator extends AbstractCommandUsageGenerator {

    @Override
    public void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, OutputStream output) throws IOException {
        final String NEW_PARA = "\n\n";

        Writer writer = new OutputStreamWriter(output);

        writer.append(programName).append("_");
        writer.append(groupName).append("_");
        writer.append(command.getName()).append("(1) -");
        writer.append(command.getDescription()).append("\n");
        writer.append("==========");

        writer.append(NEW_PARA).append("## SYNOPSIS").append(NEW_PARA);
        List<OptionMetadata> options = newArrayList();
        List<OptionMetadata> aOptions;
        if (programName != null) {
            writer.append("`").append(programName).append("`");
            aOptions = command.getGlobalOptions();
            if (aOptions != null && aOptions.size() > 0) {
                writer.append(" ").append(Joiner.on(" ").join(toSynopsisUsage(sortOptions(aOptions))));
                options.addAll(aOptions);
            }
        }
        if (groupName != null) {
            writer.append(" `").append(groupName).append("`");
            aOptions = command.getGroupOptions();
            if (aOptions != null && aOptions.size() > 0) {
                writer.append(" ").append(Joiner.on(" ").join(toSynopsisUsage(sortOptions(aOptions))));
                options.addAll(aOptions);
            }
        }
        aOptions = command.getCommandOptions();
        writer.append(" `").append(command.getName()).append("` ")
                .append(Joiner.on(" ").join(toSynopsisUsage(sortOptions(aOptions))));
        options.addAll(aOptions);

        // command arguments (optional)
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            writer.append(" [--] ").append(toUsage(arguments));
        }

        if (options.size() > 0 || arguments != null) {
            writer.append(NEW_PARA).append("## OPTIONS");
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

            if (arguments != null) {
                // "--" option
                writer.append(NEW_PARA).append("* --:\n");

                // description
                writer.append("This option can be used to separate command-line options from the "
                        + "list of arguments (useful when arguments might be mistaken for command-line options).");

                // arguments name
                writer.append(NEW_PARA).append("* ").append(toDescription(arguments)).append(":\n");

                // description
                writer.append(arguments.getDescription());
            }
        }

        if (command.getDiscussion() != null) {
            writer.append(NEW_PARA).append("## DISCUSSION").append(NEW_PARA);
            writer.append(command.getDiscussion());
        }

        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            writer.append(NEW_PARA).append("## EXAMPLES");

            // this will only work for "well-formed" examples
            for (int i = 0; i < command.getExamples().size(); i += 3) {
                String aText = command.getExamples().get(i).trim();
                String aEx = htmlize(command.getExamples().get(i + 1));

                if (aText.startsWith("*")) {
                    aText = aText.substring(1).trim();
                }

                writer.append(NEW_PARA).append("* ").append(aText).append(":\n");
                writer.append(aEx);
            }
        }

        // Flush the output
        writer.flush();
        output.flush();
    }
    
    @Override
    protected String toDescription(OptionMetadata option)
    {
        Set<String> options = option.getOptions();
        StringBuilder stringBuilder = new StringBuilder();

        final String argumentString;
        if (option.getArity() > 0) {
            argumentString = Joiner.on(" ").join(Lists.transform(ImmutableList.of(option.getTitle()), new Function<String, String>()
            {
                public String apply(@Nullable String argument)
                {
                    return "<" + argument + ">";
                }
            }));
        } else {
            argumentString = null;
        }

        Joiner.on(", ").appendTo(stringBuilder, transform(options, new Function<String, String>()
        {
            public String apply(@Nullable String option)
            {
                if (argumentString != null) {
                    return "`" + option + "` " + argumentString;
                }
                return "`" + option + "`";
            }
        }));

        return stringBuilder.toString();
    }
}
