package com.github.rvesse.airline.restrictions.options;

import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.utils.predicates.parser.ParsedOptionFinder;
import com.github.rvesse.airline.utils.predicates.restrictions.RequiredFromFinder;
import com.github.rvesse.airline.utils.predicates.restrictions.RequiredTagOptionFinder;

public class RequireFromRestriction implements OptionRestriction {

    private final String tag;
    private boolean mutuallyExclusive;

    public RequireFromRestriction(String tag, boolean mutuallyExclusive) {
        this.tag = tag;
        this.mutuallyExclusive = mutuallyExclusive;
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {

        Collection<Pair<OptionMetadata, Object>> parsedOptions = CollectionUtils.select(state.getParsedOptions(),
                new ParsedOptionFinder(option));
        if (!parsedOptions.isEmpty() && !mutuallyExclusive)
            return;

        Collection<OptionRestriction> restrictions = CollectionUtils.select(option.getRestrictions(),
                new RequiredFromFinder(this.tag));
        
        for (OptionRestriction restriction : restrictions) {
            // Find other parsed options which have the same tag
            Collection<Pair<OptionMetadata, Object>> otherParsedOptions = CollectionUtils.select(
                    state.getParsedOptions(), new RequiredTagOptionFinder(this.tag));

            // There are some parsed options but ONLY for this option
            if (otherParsedOptions.size() > 0 && otherParsedOptions.size() == parsedOptions.size())
                continue;
            
            // Otherwise may need to error
            if (mutuallyExclusive && otherParsedOptions.size() > parsedOptions.size()) {
                // TODO Throw "Only one from the set of options may be set"
            } else if (otherParsedOptions.size() == 0) {
                // TODO Throw "At least one from the set of options must be set"
            }
        }
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        // Nothing to do
    }

    public String getTag() {
        return tag;
    }
}
