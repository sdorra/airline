package com.github.rvesse.airline.restrictions.options;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingException;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;

public class RequiredOnlyIfRestriction implements OptionRestriction {

    private final Set<String> names = new LinkedHashSet<>();

    public RequiredOnlyIfRestriction(String... names) {
        this.names.addAll(AirlineUtils.arrayToList(names));
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {
        if (this.names.isEmpty())
            return;

        Collection<Pair<OptionMetadata, Object>> parsedOptions = CollectionUtils.select(state.getParsedOptions(),
                new ParsedOptionFinder(option));

        // If this option was seen then the required criteria has been fulfilled
        // regardless of whether any of the triggering options was actually
        // present
        if (parsedOptions.size() > 0)
            return;

        // Were any of the options that would trigger the required restriction
        // present?
        for (Pair<OptionMetadata, Object> otherOption : state.getParsedOptions()) {
            if (otherOption.getLeft().equals(option))
                continue;
            
            for (String name : this.names) {
                if (otherOption.getLeft().getOptions().contains(name))
                    throw new ParseOptionMissingException(option.getTitle());
            }
        }
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        // Does nothing
    }

}
