package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.OptionMetadata;

public class RequiredTagParsedOptionFinder implements Predicate<Pair<OptionMetadata, Object>> {
    
    private final String tag;
    
    public RequiredTagParsedOptionFinder(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean evaluate(Pair<OptionMetadata, Object> parsedOption) {
        return CollectionUtils.exists(parsedOption.getLeft().getRestrictions(), new RequiredFromFinder(tag));
    }

}
