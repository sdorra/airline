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
package com.github.rvesse.airline.help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * Abstract command usage generator
 * 
 */
public abstract class AbstractCommandUsageGenerator extends AbstractUsageGenerator implements CommandUsageGenerator {

    private final Comparator<? super Entry<Integer, String>> exitCodeComparator;

    public AbstractCommandUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR);
    }
    
    public AbstractCommandUsageGenerator(boolean includeHidden) {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_EXIT_CODE_COMPARATOR, includeHidden);
    }

    public AbstractCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator) {
        this(optionComparator, UsageHelper.DEFAULT_EXIT_CODE_COMPARATOR, false);
    }
    
    public AbstractCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator, boolean includeHidden) {
        this(optionComparator, UsageHelper.DEFAULT_EXIT_CODE_COMPARATOR, includeHidden);
    }

    public AbstractCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super Entry<Integer, String>> exitCodeComparator, boolean includeHidden) {
        super(optionComparator, UsageHelper.DEFAULT_COMMAND_COMPARATOR, includeHidden);
        this.exitCodeComparator = exitCodeComparator;
    }

    @Override
    public void usage(String programName, String groupName, String commandName, CommandMetadata command)
            throws IOException {
        usage(programName, groupName, commandName, command, System.out);
    }

    /**
     * Sorts the exit codes assuming a non-null comparator was provided at
     * instantiation time
     * 
     * @param exitCodes
     *            Exit codes
     * @return Sorted exit codes
     */
    protected List<Entry<Integer, String>> sortExitCodes(List<Entry<Integer, String>> exitCodes) {
        if (exitCodeComparator != null) {
            exitCodes = new ArrayList<>(exitCodes);
            Collections.sort(exitCodes, exitCodeComparator);
        }
        return exitCodes;
    }
}