package io.airlift.airline.help.html;

import static com.google.common.collect.Lists.newArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import io.airlift.airline.help.AbstractCommandUsageGenerator;
import io.airlift.airline.help.UsageHelper;
import io.airlift.airline.model.ArgumentsMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

/**
 * A usage generator that generates HTML documentation
 */
public class HtmlCommandUsageGenerator extends AbstractCommandUsageGenerator {

    public static final String DEFAULT_STYLESHEET = "css/bootstrap.min.css";

    protected final List<String> stylesheetUrls = newArrayList();

    public HtmlCommandUsageGenerator() {
        this(DEFAULT_STYLESHEET);
    }

    public HtmlCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator) {
        this(optionComparator, DEFAULT_STYLESHEET);
    }

    public HtmlCommandUsageGenerator(String stylesheetUrl) {
        this(stylesheetUrl, UsageHelper.DEFAULT_OPTION_COMPARATOR);
    }

    public HtmlCommandUsageGenerator(String stylesheetUrl, Comparator<OptionMetadata> optionComparator) {
        this(optionComparator, stylesheetUrl);
    }

    public HtmlCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator, String... stylesheetUrls) {
        super(optionComparator);
        if (stylesheetUrls != null) {
            for (String stylesheet : stylesheetUrls) {
                if (StringUtils.isNotEmpty(stylesheet)) {
                    this.stylesheetUrls.add(stylesheet);
                }
            }
        }
    }

    protected void addAdditionalCss(Writer writer) throws IOException {
        writer.append("    body { margin: 50px; }\n");
    }

    @Override
    public void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, OutputStream output) throws IOException {

        Writer writer = new OutputStreamWriter(output);

        final String NEWLINE = "<br/>\n";

        writer.append("<html>\n");
        createHtmlHead(writer);
        writer.append("<body>\n");

        createPageHeader(programName, groupName, command, writer);

        writer.append("<h1 class=\"text-info\">NAME</h1>\n").append(NEWLINE);

        writer.append("<div class=\"row\">");
        writer.append("<div class=\"span8 offset1\">");
        writer.append(programName).append(" ");
        writer.append(groupName).append(" ");
        writer.append(command.getName()).append(" ");
        writer.append("&mdash;");
        writer.append(htmlize(command.getDescription()));
        writer.append("</div>\n");
        writer.append("</div>\n");

        writer.append(NEWLINE);
        writer.append("<h1 class=\"text-info\">SYNOPSIS</h1>\n").append(NEWLINE);

        List<OptionMetadata> options = newArrayList();
        writer.append("<div class=\"row\">\n");
        writer.append("<div class=\"span8 offset1\">\n");

        if (programName != null) {
            writer.append(programName).append(" ")
                    .append(htmlize(Joiner.on(" ").join(toSynopsisUsage(sortOptions(command.getGlobalOptions())))));
            options.addAll(command.getGlobalOptions());
        }
        if (groupName != null) {
            writer.append(groupName).append(" ")
                    .append(htmlize(Joiner.on(" ").join(toSynopsisUsage(sortOptions(command.getGroupOptions())))));
            options.addAll(command.getGroupOptions());
        }
        writer.append(command.getName()).append(" ")
                .append(htmlize(Joiner.on(" ").join(toSynopsisUsage(sortOptions(command.getCommandOptions())))));
        options.addAll(command.getCommandOptions());

        // command arguments (optional)
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            writer.append(" [--] ").append(htmlize(toUsage(arguments)));
        }

        writer.append("</div>\n");
        writer.append("</div>\n");

        //
        // OPTIONS
        //
        if (options.size() > 0 || arguments != null) {
            options = sortOptions(options);

            writer.append(NEWLINE);
            writer.append("<h1 class=\"text-info\">OPTIONS</h1>\n").append(NEWLINE);

            for (OptionMetadata option : options) {
                // skip hidden options
                if (option.isHidden()) {
                    continue;
                }

                // option names
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset1\">\n");
                writer.append(htmlize(toDescription(option)));
                writer.append("</div>\n");
                writer.append("</div>\n");

                // description
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset2\">\n");
                writer.append(htmlize(option.getDescription()));
                writer.append("</div>\n");
                writer.append("</div>\n");
                
                // allowedValues
                if (option.getAllowedValues() != null && option.getAllowedValues().size() > 0 && option.getArity() >= 1) {
                    writer.append("<div class=\"row\">\n");
                    writer.append("<div class=\"span8 offset3\">\n");
                    writer.append("This options value");
                    if (option.getArity() == 1) {
                        writer.append(" is ");
                    } else {
                        writer.append("s are ");
                    }
                    writer.append("restricted to the following value(s):\n");
                    
                    writer.append("<ul>");
                    for (String value : option.getAllowedValues()) {
                        writer.append("<li>").append(value).append("</li>\n");
                    }
                    writer.append("</ul>");
                    writer.append("</div>\n");
                    writer.append("</div>\n");
                }
            }

            if (arguments != null) {
                // "--" option
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset1\">\n");

                writer.append("--\n");

                writer.append("</div>\n");
                writer.append("</div>\n");

                // description
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset2\">\n");

                writer.append("This option can be used to separate command-line options from the "
                        + "list of argument, (useful when arguments might be mistaken for command-line options)\n");

                writer.append("</div>\n");
                writer.append("</div>\n");

                // arguments name
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset1\">\n");

                writer.append(htmlize(toDescription(arguments)));

                writer.append("</div>\n");
                writer.append("</div>\n");

                // description
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset2\">\n");

                writer.append(htmlize(arguments.getDescription()));

                writer.append("</div>\n");
                writer.append("</div>\n");
            }
        }

        if (command.getDiscussion() != null) {
            writer.append(NEWLINE);
            writer.append("<h1 class=\"text-info\">DISCUSSION</h1>\n").append(NEWLINE);

            writer.append("<div class=\"row\">\n");
            writer.append("<div class=\"span8 offset1\">\n");

            writer.append(htmlize(command.getDiscussion()));

            writer.append("</div>\n");
            writer.append("</div>\n");
        }

        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            writer.append(NEWLINE);
            writer.append("<h1 class=\"text-info\">EXAMPLES</h1>\n").append(NEWLINE);

            writer.append("<div class=\"row\">\n");
            writer.append("<div class=\"span12 offset1\">\n");

            // this will only work for "well-formed" examples
            for (String example : command.getExamples()) {
                writer.append("<p>\n");
                writer.append(example);
                writer.append("</p>\n");
            }

            writer.append("</div>\n");
            writer.append("</div>\n");
        }

        if (command.getExitCodes() != null && !command.getExitCodes().isEmpty()) {
            writer.append(NEWLINE);
            writer.append("<h1 class=\"text-info\">EXIT STATUS</h1>\n").append(NEWLINE);

            writer.append("<p>\n");
            writer.append("  The ");
            if (programName != null) {
                writer.append(programName).append(" ");
            }
            if (groupName != null) {
                writer.append(groupName).append(" ");
            }
            writer.append(commandName).append(" command exits with one of the following values:");
            writer.append("</p>\n");

            for (Entry<Integer, String> exit : sortExitCodes(Lists.newArrayList(command.getExitCodes().entrySet()))) {
                writer.append("<div class=\"row\">\n");
                writer.append("<div class=\"span8 offset1\">\n");

                // Print the exit code
                writer.append(exit.getKey().toString());

                // Include description if available
                if (!StringUtils.isEmpty(exit.getValue())) {
                    writer.append("</div>\n");
                    writer.append("</div>\n");
                    
                    writer.append("<div class=\"row\">\n");
                    writer.append("<div class=\"span8 offset2\">\n");
                    
                    writer.append(htmlize(exit.getValue()));
                    
                    writer.append("</div>\n");
                    writer.append("</div>\n");
                } else {
                    writer.append("</div>\n");
                    writer.append("</div>\n");
                }
            }
        }

        writer.append("</body>\n");
        writer.append("</html>\n");

        // Flush the output
        writer.flush();
        output.flush();
    }

    protected void createPageHeader(String programName, String groupName, CommandMetadata command, Writer writer)
            throws IOException {
        writer.append("<hr/>\n");
        writer.append("<h1 class=\"text-info\">").append(programName).append(" ").append(groupName).append(" ")
                .append(command.getName()).append(" Manual Page\n");
        writer.append("<hr/>\n");
    }

    protected void createHtmlHead(Writer writer) throws IOException {
        writer.append("<head>\n");
        for (String stylesheet : this.stylesheetUrls) {
            writer.append("<link href=\"").append(stylesheet).append("\" rel=\"stylesheet\">\n");
        }
        writer.append("<style>\n");
        addAdditionalCss(writer);
        writer.append("</style>\n");
        writer.append("</head>\n");
    }

}
