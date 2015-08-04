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

import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.cli.CliUsageHelper;
import com.github.rvesse.airline.help.common.AbstractUsageGenerator;
import com.github.rvesse.airline.help.common.UsagePrinter;
import com.github.rvesse.airline.help.sections.HelpFormat;
import com.github.rvesse.airline.help.sections.HelpHint;
import com.github.rvesse.airline.help.sections.HelpSection;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class RonnUsageHelper extends AbstractUsageGenerator {

    /**
     * Constant for a new paragraph
     */
    public static final String NEW_PARA = "\n\n";
    /**
     * Constant for a horizontal rule
     */
    public static final String HORIZONTAL_RULE = "---";

    public RonnUsageHelper(Comparator<? super OptionMetadata> optionComparator, boolean includeHidden) {
        super(optionComparator, UsageHelper.DEFAULT_COMMAND_COMPARATOR, includeHidden);
    }

    public void outputArguments(Writer writer, CommandMetadata command) throws IOException {
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            // Arguments separator
            writer.append(NEW_PARA).append("* `").append(ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR).append("`:\n");

            // description
            writer.append("This option can be used to separate command-line options from the "
                    + "list of arguments (useful when arguments might be mistaken for command-line options).");

            // arguments name
            writer.append(NEW_PARA).append("* ").append(toDescription(arguments)).append(":\n");

            // description
            writer.append(arguments.getDescription());

            // Restrictions
            for (ArgumentsRestriction restriction : arguments.getRestrictions()) {
                if (restriction instanceof HelpHint) {
                    outputArgumentsRestriction(writer, arguments, restriction, (HelpHint) restriction);
                }
            }
        }
    }

    protected void outputArgumentsRestriction(Writer writer, ArgumentsMetadata arguments,
            ArgumentsRestriction restriction, HelpHint hint) throws IOException {
        outputHint(writer, hint, true, 2);
    }

    public void outputOptions(Writer writer, List<OptionMetadata> options, String sectionHeader) throws IOException {
        writer.append(NEW_PARA).append(sectionHeader).append("OPTIONS");
        options = sortOptions(options);

        for (OptionMetadata option : options) {
            // skip hidden options
            if (option.isHidden() && !this.includeHidden()) {
                continue;
            }

            // option names
            writer.append(NEW_PARA).append("* ").append(toDescription(option)).append(":\n");

            // description
            writer.append("  ").append(option.getDescription());

            // Restrictions
            for (OptionRestriction restriction : option.getRestrictions()) {
                if (restriction instanceof HelpHint) {
                    outputOptionRestriction(writer, option, restriction, (HelpHint) restriction);
                }
            }
        }
    }

    /**
     * Outputs a documentation section detailing the allowed values for an
     * option
     * 
     * @param writer
     *            Writer
     * @param option
     *            Option meta-data
     * @param restriction
     *            Restriction
     * @param hint
     *            Help hint
     * @throws IOException
     */
    protected void outputOptionRestriction(Writer writer, OptionMetadata option, OptionRestriction restriction,
            HelpHint hint) throws IOException {
        outputHint(writer, hint, true, 2);

    }

    protected void outputHint(Writer writer, HelpHint hint, boolean requireNewPara, int baseIndent) throws IOException {
        // Skip non-printable hints
        if (hint.getFormat() == HelpFormat.NONE_PRINTABLE)
            return;

        UsagePrinter printer = new UsagePrinter(writer, Integer.MAX_VALUE);
        if (baseIndent > 0)
            printer = printer.newIndentedPrinter(baseIndent);

        if (requireNewPara)
            printer.newline().newline();

        // Pre-amble
        if (!StringUtils.isBlank(hint.getPreamble())) {
            printer.append(hint.getPreamble());
            printer.newline().newline();
        }

        // Hint content
        switch (hint.getFormat()) {
        case EXAMPLES:
            String[] examples = hint.getContentBlock(0);
            for (int e = 0; e < examples.length; e++) {
                printer.flush();

                UsagePrinter examplePrinter = printer.newIndentedPrinter(4);
                examplePrinter.appendOnOneLine(examples[e]);
                examplePrinter.newline().newline();
                examplePrinter.flush();

                for (int d = 1; d < hint.numContentBlocks(); d++) {
                    String[] descriptions = hint.getContentBlock(d);
                    if (e >= descriptions.length)
                        continue;

                    printer.append(descriptions[e]);
                    printer.newline().newline();
                    printer.flush();
                }
            }
            break;
        case TABLE:
        case TABLE_WITH_HEADERS:
            // Table
            // HACK: Ronn/Markdown doesn't support tables properly so we'll just
            // print as a list with the first column emboldened
            int maxRows = CliUsageHelper.calculateMaxRows(hint);
            for (int row = 0; row < maxRows; row++) {
                printer.append("* ");
                for (int col = 0; col < hint.numContentBlocks(); col++) {
                    String[] colData = hint.getContentBlock(col);
                    if (row < colData.length) {
                        if (StringUtils.isEmpty(colData[row]))
                            continue;
                        if (col > 0)
                            printer.append(" - ");

                        if (col == 0 || (row == 0 && hint.getFormat() == HelpFormat.TABLE_WITH_HEADERS))
                            printer.append("**");
                        printer.append(colData[row]);
                        if (col == 0 || (row == 0 && hint.getFormat() == HelpFormat.TABLE_WITH_HEADERS))
                            printer.append("**");

                    }
                }
                printer.newline();
            }
            break;
        case LIST:
            // List
            if (!requireNewPara && StringUtils.isBlank(hint.getPreamble()))
                printer.newline().newline();

            // Nested list so pad inwards
            String[] items = hint.getContentBlock(0);
            for (int i = 0; i < items.length; i++) {
                // HACK: Nested lists are actually broken in RONN
                // However 7 is the magical number which makes things line up
                // when RONN generates the TROFF output (go figure)
                UsagePrinter listPrinter = printer.newIndentedPrinter(7);
                listPrinter.append(items[i]).newline();
                listPrinter.flush();
            }
            break;
        default:
            // Prose
            for (int i = 0; i < hint.numContentBlocks(); i++) {
                for (String para : hint.getContentBlock(i)) {
                    printer.append(para);
                    printer.newline().newline();
                }
            }
            break;
        }

        printer.flush();
        writer.flush();
    }

    @Override
    protected String toDescription(OptionMetadata option) {
        Set<String> options = option.getOptions();
        StringBuilder stringBuilder = new StringBuilder();

        final String argumentString;
        if (option.getArity() > 0) {
            argumentString = String.format("<%s>", option.getTitle());
        } else {
            argumentString = null;
        }

        boolean first = true;
        for (String name : options) {
            if (!first) {
                stringBuilder.append(", ");
            } else {
                first = false;
            }
            stringBuilder.append('`').append(name).append('`');
            if (argumentString != null)
                stringBuilder.append(' ').append(argumentString);
        }

        return stringBuilder.toString();
    }

    /**
     * Outputs a help section
     * 
     * @param writer
     *            Writer
     * @param section
     *            Help section
     * @throws IOException
     */
    public void outputHelpSection(Writer writer, HelpSection section, String sectionHeader) throws IOException {
        if (section.getFormat() == HelpFormat.NONE_PRINTABLE)
            return;

        // Section title
        if (!StringUtils.isBlank(section.getTitle())) {
            writer.append(NEW_PARA).append(sectionHeader);
            writer.append(section.getTitle().toUpperCase());
            writer.append(NEW_PARA);
        } else {
            writer.append(NEW_PARA);
        }

        // Content
        outputHint(writer, section, false, 0);

        // Post-amble
        if (!StringUtils.isBlank(section.getPostamble())) {
            writer.append(section.getPostamble());
            writer.append(NEW_PARA);
        }

        writer.flush();
    }
}
