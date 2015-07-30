package com.github.rvesse.airline.help.cli;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.common.AbstractUsageGenerator;
import com.github.rvesse.airline.help.common.UsagePrinter;
import com.github.rvesse.airline.help.sections.HelpHint;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class CliUsageGeneratorHelper extends AbstractUsageGenerator {

    public CliUsageGeneratorHelper(Comparator<? super OptionMetadata> optionComparator, boolean includeHidden) {
        super(optionComparator, UsageHelper.DEFAULT_COMMAND_COMPARATOR, includeHidden);
    }

    public void outputOptions(UsagePrinter out, List<OptionMetadata> options) throws IOException {
        out.append("OPTIONS").newline();

        options = sortOptions(options);
        for (OptionMetadata option : options) {
            // Skip hidden options
            if (option.isHidden() && !this.includeHidden()) {
                continue;
            }

            // Option names
            UsagePrinter optionPrinter = out.newIndentedPrinter(8);
            optionPrinter.append(toDescription(option)).newline();
            optionPrinter.flush();

            // Description
            UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
            descriptionPrinter.append(option.getDescription()).newline();

            // Restrictions
            for (OptionRestriction restriction : option.getRestrictions()) {
                if (restriction instanceof HelpHint) {
                    outputOptionRestriction(descriptionPrinter, option, restriction, (HelpHint) restriction);
                }
            }

            descriptionPrinter.newline();
            descriptionPrinter.flush();
        }
    }

    /**
     * Outputs documentation about a restriction on an option
     * 
     * @param descriptionPrinter
     *            Description printer
     * @param option
     *            Option meta-data
     * @param restriction
     *            Restriction
     * @param hint
     *            Help hint
     * @throws IOException
     */
    protected void outputOptionRestriction(UsagePrinter descriptionPrinter, OptionMetadata option,
            OptionRestriction restriction, HelpHint hint) throws IOException {
        outputRestriction(descriptionPrinter, hint);
    }

    protected void outputRestriction(UsagePrinter descriptionPrinter, HelpHint hint) throws IOException {
        descriptionPrinter.newline();

        // Print preamble if present
        if (!StringUtils.isEmpty(hint.getPreamble())) {
            descriptionPrinter.append(hint.getPreamble());
        }

        // Print more details if there are some content blocks present
        if (hint.numContentBlocks() > 0) {
            // Decide how to print based on the format
            switch (hint.getFormat()) {
            case TABLE:
            case TABLE_WITH_HEADERS:
                // TODO Print as table
                // UsagePrinter tablePrinter =
                // descriptionPrinter.newIndentedPrinter(4);
                // tablePrinter.appendTable(table, rowSpacing)
                break;
            case LIST:
                // Print first content block as an indented list list
                UsagePrinter listPrinter = descriptionPrinter.newIndentedPrinter(4);
                for (String item : hint.getContentBlock(0)) {
                    descriptionPrinter.append(item).newline();
                }
                listPrinter.flush();
                break;
            default:
                // Print first content block as text
                descriptionPrinter.newline();
                for (String line : hint.getContentBlock(0)) {
                    descriptionPrinter.append(line).newline();
                }
                break;
            }
        }
    }

    public void outputArguments(UsagePrinter out, ArgumentsMetadata arguments) throws IOException {
        if (arguments != null) {
            // Arguments separator option
            UsagePrinter optionPrinter = out.newIndentedPrinter(8);
            optionPrinter.append(ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR).newline();
            optionPrinter.flush();

            // Description
            UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
            descriptionPrinter
                    .append("This option can be used to separate command-line options from the list of argument, (useful when arguments might be mistaken for command-line options)")
                    .newline();
            descriptionPrinter.newline();

            // Arguments name(s)
            optionPrinter.append(toDescription(arguments)).newline();

            // Description
            descriptionPrinter.append(arguments.getDescription()).newline();

            // Restrictions
            for (ArgumentsRestriction restriction : arguments.getRestrictions()) {
                if (restriction instanceof HelpHint) {
                    outputArgumentsRestriction(descriptionPrinter, arguments, restriction, (HelpHint) restriction);
                }
            }

            descriptionPrinter.newline();
            descriptionPrinter.flush();
        }
    }

    /**
     * Outputs documentation about a restriction on an option
     * 
     * @param descriptionPrinter
     *            Description printer
     * @param arguments
     *            Arguments meta-data
     * @param restriction
     *            Restriction
     * @param hint
     *            Help hint
     * @throws IOException
     */
    protected void outputArgumentsRestriction(UsagePrinter descriptionPrinter, ArgumentsMetadata arguments,
            ArgumentsRestriction restriction, HelpHint hint) throws IOException {
        outputRestriction(descriptionPrinter, hint);
    }
}
