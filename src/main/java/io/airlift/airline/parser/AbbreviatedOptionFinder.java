package io.airlift.airline.parser;

import java.util.Collection;
import io.airlift.airline.model.OptionMetadata;

public final class AbbreviatedOptionFinder extends AbstractAbbreviationFinder<OptionMetadata> {

    public AbbreviatedOptionFinder(String value, Collection<OptionMetadata> items) {
        super(value, items);
    }

    @Override
    protected boolean isExactNameMatch(String value, OptionMetadata item) {
        return item.getOptions().contains(value);
    }

    @Override
    protected boolean isPartialNameMatch(String value, OptionMetadata item) {
        for (String name : item.getOptions()) {
            if (name.length() <= 2)
                continue;
            if (name.startsWith(value))
                return true;
        }
        return false;
    }

}
