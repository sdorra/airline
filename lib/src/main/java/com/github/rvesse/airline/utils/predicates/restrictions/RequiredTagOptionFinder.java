package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import com.github.rvesse.airline.model.OptionMetadata;

public class RequiredTagOptionFinder implements Predicate<OptionMetadata> {

    private final String tag;

    public RequiredTagOptionFinder(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean evaluate(OptionMetadata arg0) {
        return CollectionUtils.exists(arg0.getRestrictions(), new RequiredFromFinder(tag));
    }

}
