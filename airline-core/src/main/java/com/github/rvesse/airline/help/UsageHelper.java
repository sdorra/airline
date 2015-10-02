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

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class UsageHelper {
    /**
     * Default comparator for options
     * <p>
     * Compares against the user readable portion of the option name omitting
     * any leading {@code -} characters
     * </p>
     */
    public static final Comparator<OptionMetadata> DEFAULT_OPTION_COMPARATOR = new Comparator<OptionMetadata>() {
        @Override
        public int compare(OptionMetadata o1, OptionMetadata o2) {
            String option1 = o1.getOptions().iterator().next();
            option1 = option1.replaceFirst("^-+", "");

            String option2 = o2.getOptions().iterator().next();
            option2 = option2.replaceFirst("^-+", "");

            int c = option1.toLowerCase().compareTo(option2.toLowerCase());
            if (c == 0) {
                c = option2.compareTo(option1);
                if (c == 0) {
                    c = Integer.compare(System.identityHashCode(option1), System.identityHashCode(option2));
                }
            }
            return c;
        }
    };

    /**
     * Default comparator for commands
     * <p>
     * Compares by alphabetical ordering
     * </p>
     */
    public static final Comparator<CommandMetadata> DEFAULT_COMMAND_COMPARATOR = new Comparator<CommandMetadata>() {
        @Override
        public int compare(CommandMetadata command1, CommandMetadata command2) {
            int c = command1.getName().toLowerCase().compareTo(command2.getName().toLowerCase());
            if (c == 0) {
                c = command2.getName().compareTo(command1.getName());
                if (c == 0) {
                    c = Integer.compare(System.identityHashCode(command1), System.identityHashCode(command2));
                }
            }
            return c;
        }
    };

    public static final Comparator<CommandGroupMetadata> DEFAULT_COMMAND_GROUP_COMPARATOR = new Comparator<CommandGroupMetadata>() {
        @Override
        public int compare(CommandGroupMetadata group1, CommandGroupMetadata group2) {
            int c = group1.getName().toLowerCase().compareTo(group2.getName().toLowerCase());
            if (c == 0) {
                c = group2.getName().compareTo(group1.getName());
                if (c == 0) {
                    c = Integer.compare(System.identityHashCode(group1), System.identityHashCode(group2));
                }
            }
            return c;
        }
    };

    /**
     * Default comparator for exit codes
     * <p>
     * Compares by numerical sorting on the exit codes and then alphabetical
     * sorting on the descriptions
     * </p>
     */
    public static final Comparator<Entry<Integer, String>> DEFAULT_EXIT_CODE_COMPARATOR = new Comparator<Entry<Integer, String>>() {
        @Override
        public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
            int c = Integer.compare(o1.getKey(), o2.getKey());
            if (c == 0) {
                c = o1.getValue().compareTo(o2.getValue());
                if (c == 0) {
                    c = Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2));
                }
            }
            return c;
        }
    };

    public static String[] toGroupNames(List<CommandGroupMetadata> groupPath) {
        String[] groupNames = new String[groupPath.size()];
        for (int i = 0; i < groupPath.size(); i++) {
            groupNames[i] = groupPath.get(i).getName();
        }
        return groupNames;
    }
}
