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
import com.github.rvesse.airline.help.common.AbstractUsageGenerator;
import com.github.rvesse.airline.help.sections.HelpHint;
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
        outputRestriction(writer, hint);
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
            writer.append(option.getDescription());

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
        outputRestriction(writer, hint);

    }

    protected void outputRestriction(Writer writer, HelpHint hint) throws IOException {
        if (!StringUtils.isEmpty(hint.getPreamble())) {
            writer.append(NEW_PARA).append("  ").append(hint.getPreamble());
        }

        switch (hint.getFormat()) {
        case LIST:
            writer.append(" [");
            writer.append(StringUtils.join(hint.getContentBlock(0), ", "));
            writer.append("]");
            break;
        default:
            for (int i = 0; i < hint.numContentBlocks(); i++) {
                for (String para : hint.getContentBlock(i)) {
                    writer.append(NEW_PARA).append("  ");
                    writer.append(para);
                }
            }
            break;
        }
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
}
