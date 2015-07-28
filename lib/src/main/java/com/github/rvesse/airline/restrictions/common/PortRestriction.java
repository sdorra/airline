package com.github.rvesse.airline.restrictions.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.annotations.restrictions.PortType;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseInvalidRestrictionException;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;

public class PortRestriction extends AbstractRestriction {
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

    protected void invalidArgumentsPort(ArgumentsMetadata arguments, Object value) {
        invalidPort(String.format("Argument '%s'", arguments.getTitle().get(0)), value);
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
        for (Object value : values) {
            if (value instanceof Long) {
                if (!isValid(((Long) value).longValue()))
                    invalidArgumentsPort(arguments, value);
            } else if (value instanceof Integer) {
                if (!isValid(((Integer) value).intValue()))
                    invalidArgumentsPort(arguments, value);
            } else if (value instanceof Short) {
                if (!isValid(((Short) value).shortValue()))
                    invalidArgumentsPort(arguments, value);
            }
            throw new ParseInvalidRestrictionException("Cannot apply a @Port restriction to an option of type %s",
                    arguments.getJavaType());
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
}
