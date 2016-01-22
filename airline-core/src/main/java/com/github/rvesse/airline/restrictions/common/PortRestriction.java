/**
 * Copyright (C) 2010-16 the original author or authors.
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
package com.github.rvesse.airline.restrictions.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.annotations.restrictions.PortType;
import com.github.rvesse.airline.help.sections.HelpFormat;
import com.github.rvesse.airline.help.sections.HelpHint;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseInvalidRestrictionException;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;

public class PortRestriction extends AbstractCommonRestriction implements HelpHint {
    private static final int MIN_PORT = 0, MAX_PORT = 65535;

    private Set<PortType> acceptablePorts = new HashSet<>();

    public PortRestriction(PortType... portTypes) {
        this.acceptablePorts.addAll(AirlineUtils.arrayToList(portTypes));
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {
        if (acceptablePorts.isEmpty())
            return;

        Collection<Pair<OptionMetadata, Object>> parsedOptions = CollectionUtils.select(state.getParsedOptions(),
                new ParsedOptionFinder(option));
        if (parsedOptions.isEmpty())
            return;

        for (Pair<OptionMetadata, Object> parsedOption : parsedOptions) {
            Object value = parsedOption.getRight();
            if (value instanceof Long) {
                if (!isValid(((Long) value).longValue()))
                    invalidOptionPort(option, value);
            } else if (value instanceof Integer) {
                if (!isValid(((Integer) value).intValue()))
                    invalidOptionPort(option, value);
            } else if (value instanceof Short) {
                if (!isValid(((Short) value).shortValue()))
                    invalidOptionPort(option, value);
            } else {
                throw new ParseInvalidRestrictionException("Cannot apply a @Port restriction to an option of type %s",
                        option.getJavaType());
            }
        }
    }

    protected void invalidOptionPort(OptionMetadata option, Object value) {
        invalidPort(String.format("Option '%s'", option.getTitle()), value);
    }

    protected void invalidArgumentsPort(ArgumentsMetadata arguments, int argIndex, Object value) {
        invalidPort(String.format("Argument '%s'", AbstractCommonRestriction.getArgumentTitle(arguments, argIndex)),
                value);
    }

    protected void invalidPort(String title, Object value) {
        throw new ParseRestrictionViolatedException(
                "%s which takes a port number was given a value '%s' which not in the range of acceptable ports: %s",
                title, value, PortType.toRangesString(acceptablePorts));
    }

    @Override
    public <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments) {
        if (acceptablePorts.isEmpty())
            return;

        if (state.getParsedArguments().isEmpty())
            return;

        List<Object> values = state.getParsedArguments();
        int i = 0;
        for (Object value : values) {
            if (value instanceof Long) {
                if (!isValid(((Long) value).longValue()))
                    invalidArgumentsPort(arguments, i, value);
            } else if (value instanceof Integer) {
                if (!isValid(((Integer) value).intValue()))
                    invalidArgumentsPort(arguments, i, value);
            } else if (value instanceof Short) {
                if (!isValid(((Short) value).shortValue()))
                    invalidArgumentsPort(arguments, i, value);
            } else {
                throw new ParseInvalidRestrictionException("Cannot apply a @Port restriction to an option of type %s",
                        arguments.getJavaType());
            }
            i++;
        }
    }

    private boolean isValid(long port) {
        if (port < MIN_PORT || port > MAX_PORT)
            return false;
        if (this.acceptablePorts.contains(PortType.ANY))
            return true;

        return inAnyAcceptableRange((int) port);
    }

    private boolean isValid(int port) {
        if (port < MIN_PORT || port > MAX_PORT)
            return false;
        if (this.acceptablePorts.contains(PortType.ANY))
            return true;

        return inAnyAcceptableRange(port);
    }

    private boolean isValid(short port) {
        if (port < MIN_PORT || port > MAX_PORT)
            return false;
        if (this.acceptablePorts.contains(PortType.ANY))
            return true;

        return inAnyAcceptableRange((int) port);
    }

    protected boolean inAnyAcceptableRange(int port) {
        // Check acceptable port ranges
        for (PortType portType : this.acceptablePorts) {
            if (portType.inRange(port))
                return true;
        }
        return false;
    }

    @Override
    public String getPreamble() {
        return null;
    }

    @Override
    public HelpFormat getFormat() {
        return HelpFormat.PROSE;
    }

    @Override
    public int numContentBlocks() {
        return 1;
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        if (blockNumber != 0)
            throw new IndexOutOfBoundsException();
        if (this.acceptablePorts.contains(PortType.ANY)) {
            return new String[] {
                    String.format("This options value represents a port and must fall in the port range %s",
                            PortType.ANY.toString()) };
        } else {
            return new String[] { String.format(
                    "This options value represents a port and must fall in one of the following port ranges: %s",
                    PortType.toRangesString(this.acceptablePorts)) };

        }
    }
}
