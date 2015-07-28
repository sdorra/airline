package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.options.RequireFromRestriction;

public class RequiredFromFinder implements Predicate<OptionRestriction> {

    private final String tag;

    public RequiredFromFinder() {
        this(null);
    }

    public RequiredFromFinder(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean evaluate(OptionRestriction restriction) {
        if (restriction instanceof RequireFromRestriction) {
            if (tag == null)
                return true;
            RequireFromRestriction requirement = (RequireFromRestriction) restriction;
            return StringUtils.equals(tag, requirement.getTag());
        }
        return false;
    }

}
