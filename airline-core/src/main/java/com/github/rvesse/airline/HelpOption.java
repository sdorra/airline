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
package com.github.rvesse.airline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.cli.CliCommandUsageGenerator;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;

/**
 * An option that provides a simple way for the user to request help with a
 * command
 *
 */
public class HelpOption<C> {
    @Inject
    private GlobalMetadata<C> globalMetadata;

    @Inject
    private CommandGroupMetadata groupMetadata;

    @Inject
    private CommandMetadata commandMetadata;

    @Option(name = { "-h", "--help" }, description = "Display help information")
    public Boolean help = false;

    private boolean shown = false;

    /**
     * Shows help if user requested it and it hasn't already been shown using
     * the default {@link CliCommandUsageGenerator}
     * 
     * @return True if help was requested by the user
     */
    public boolean showHelpIfRequested() {
        return this.showHelpIfRequested(new CliCommandUsageGenerator());
    }

    /**
     * Shows help if user requested it and it hasn't already been shown
     * 
     * @param generator
     *            Usage generator
     * @return True if help was requested by the user
     */
    public boolean showHelpIfRequested(CommandUsageGenerator generator) {
        if (help && !shown) {
            showHelp(generator);
            shown = true;
        }
        return help;
    }

    /**
     * Shows help using the default {@link CliCommandUsageGenerator}
     */
    public void showHelp() {
        showHelp(new CliCommandUsageGenerator());
    }

    /**
     * Shows help using the given usage generator
     * 
     * @param generator
     *            Usage generator
     */
    public void showHelp(CommandUsageGenerator generator) {
        if (generator == null)
            throw new NullPointerException("Usage generator cannot be null");
        try {
            generator.usage(globalMetadata != null ? globalMetadata.getName() : null,
                    groupMetadata != null ? toGroupNames(groupMetadata) : null, commandMetadata.getName(),
                    commandMetadata, globalMetadata != null ? globalMetadata.getParserConfiguration() : null);
        } catch (IOException e) {
            throw new RuntimeException("Error generating usage documentation", e);
        }
    }

    private static String[] toGroupNames(CommandGroupMetadata group) {
        List<CommandGroupMetadata> groupPath = new ArrayList<CommandGroupMetadata>();
        groupPath.add(group);
        while (group.getParent() != null) {
            group = group.getParent();
            groupPath.add(0, group);
        }
        return UsageHelper.toGroupNames(groupPath);
    }
}
