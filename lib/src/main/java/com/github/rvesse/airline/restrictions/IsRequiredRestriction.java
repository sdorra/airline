package com.github.rvesse.airline.restrictions;

import org.apache.commons.collections4.CollectionUtils;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsMissingException;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingException;
import com.github.rvesse.airline.utils.AirlineUtils;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;

/**
 * A restriction that options/arguments are required
 */
public class IsRequiredRestriction extends AbstractRestriction {

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {
        if (CollectionUtils.find(state.getParsedOptions(), new ParsedOptionFinder(option)) == null)
            throw new ParseOptionMissingException(AirlineUtils.first(option.getOptions()));
    }

    @Override
    public <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments) {
        if (state.getParsedArguments().isEmpty())
            throw new ParseArgumentsMissingException(arguments.getTitle());
    }

}
