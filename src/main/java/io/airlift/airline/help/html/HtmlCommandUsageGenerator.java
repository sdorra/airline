package io.airlift.airline.help.html;

import static com.google.common.collect.Lists.newArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

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

    /**
     * Default stylesheet (Bootstrap)
     */
    public static final String DEFAULT_STYLESHEET = "css/bootstrap.min.css";
    /**
     * Constant for a new line (using a {@code <br>})
     */
    protected static final String NEWLINE = "<br/>\n";

    /**
     * List of stylesheet URLs
     */
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

    @Override
    public void usage( String programName,  String groupName, String commandName,
            CommandMetadata command, OutputStream output) throws IOException {

        Writer writer = new OutputStreamWriter(output);

        // Header
        outputHtmlHeader(writer);
        writer.append("<body>\n");

        // Page Header i.e. <h1>
        outputPageHeader(writer, programName, groupName, command);

        // Name and description of command
        outputDescription(writer, programName, groupName, command);

        // Synopsis
        List<OptionMetadata> options = outputSynopsis(writer, programName, groupName, command);

        // Options
        if (options.size() > 0 || command.getArguments() != null) {
            options = sortOptions(options);
            outputOptions(writer, options, command.getArguments());
        }

        // Discussion
        if (command.getDiscussion() != null) {
            outputDiscussion(writer, command);
        }

        // Examples
        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            outputExamples(writer, command);
        }

        // Exit Codes
        if (command.getExitCodes() != null && !command.getExitCodes().isEmpty()) {
            outputExitCodes(writer, programName, groupName, commandName, command);
        }

        writer.append("</body>\n");
        writer.append("</html>\n");

        // Flush the output
        writer.flush();
        output.flush();
    }

    /**
     * Outputs a documentation section detailing the exit codes
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputExitCodes(Writer writer, String programName, String groupName, String commandName,
            CommandMetadata command) throws IOException {
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

    /**
     * Outputs a documentation section detailing the examples
     * 
     * @param writer
     *            Writer
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputExamples(Writer writer, CommandMetadata command) throws IOException {
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

    /**
     * Outputs a documentation section with the discussion
     * 
     * @param writer
     *            Writer
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputDiscussion(Writer writer, CommandMetadata command) throws IOException {
        writer.append(NEWLINE);
        writer.append("<h1 class=\"text-info\">DISCUSSION</h1>\n").append(NEWLINE);

        writer.append("<div class=\"row\">\n");
        writer.append("<div class=\"span8 offset1\">\n");

        writer.append(htmlize(command.getDiscussion()));

        writer.append("</div>\n");
        writer.append("</div>\n");
    }

    /**
     * Outputs a documentation section detailing the options
     * 
     * @param writer
     *            Writer
     * @param options
     *            Option meta-data
     * @throws IOException
     */
    protected void outputOptions(Writer writer, List<OptionMetadata> options, ArgumentsMetadata arguments)
            throws IOException {
        writer.append(NEWLINE);
        writer.append("<h1 class=\"text-info\">OPTIONS</h1>\n").append(NEWLINE);

        for (OptionMetadata option : options) {
            // skip hidden options
            if (option.isHidden()) {
                continue;
            }

            // Option names
            writer.append("<div class=\"row\">\n");
            writer.append("<div class=\"span8 offset1\">\n");
            writer.append(htmlize(toDescription(option)));
            writer.append("</div>\n");
            writer.append("</div>\n");

            // Description
            writer.append("<div class=\"row\">\n");
            writer.append("<div class=\"span8 offset2\">\n");
            writer.append(htmlize(option.getDescription()));
            writer.append("</div>\n");
            writer.append("</div>\n");

            // Allowed values
            if (option.getAllowedValues() != null && option.getAllowedValues().size() > 0 && option.getArity() >= 1) {
                outputAllowedValues(writer, option);
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

    /**
     * Outputs a documentation section detailing an allowed value for an option
     * 
     * @param writer
     *            Writer
     * @param option
     *            Option meta-data
     * @throws IOException
     */
    protected void outputAllowedValues(Writer writer, OptionMetadata option) throws IOException {
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

    /**
     * Outputs a documentation section with a synopsis of the command
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param command
     *            Command name
     * @return List of all the available options (Global, Group and Command)
     * @throws IOException
     */
    protected List<OptionMetadata> outputSynopsis(Writer writer, String programName, String groupName,
            CommandMetadata command) throws IOException {
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

        return options;
    }

    /**
     * Outputs a documentation section with the name and description of the
     * command
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputDescription(Writer writer, String programName, String groupName, CommandMetadata command)
            throws IOException {
        writer.append("<h2 class=\"text-info\">NAME</h1>\n").append(NEWLINE);

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
    }

    /**
     * Outputs a page header
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputPageHeader(Writer writer, String programName, String groupName, CommandMetadata command)
            throws IOException {
        writer.append("<hr/>\n");
        writer.append("<h1 class=\"text-info\">").append(programName).append(" ").append(groupName).append(" ")
                .append(command.getName()).append(" Manual Page\n");
        writer.append("<hr/>\n");
    }

    /**
     * Outputs the HTML header for the page
     * 
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void outputHtmlHeader(Writer writer) throws IOException {
        writer.append("<html>\n");
        writer.append("<head>\n");
        outputStylesheets(writer);
        writer.append("<style>\n");
        outputAdditionalCss(writer);
        writer.append("</style>\n");
        writer.append("</head>\n");
    }

    /**
     * Outputs additional CSS directly
     * 
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void outputAdditionalCss(Writer writer) throws IOException {
        writer.append("    body { margin: 50px; }\n");
    }

    /**
     * Outputs the style sheet declarations
     * 
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void outputStylesheets(Writer writer) throws IOException {
        for (String stylesheet : this.stylesheetUrls) {
            writer.append("<link href=\"").append(stylesheet).append("\" rel=\"stylesheet\">\n");
        }
    }

}
