package com.github.rvesse.airline.parser.errors;

import java.util.Collection;
import java.util.Set;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * A parser exception that relates to a restriction violated where the violation
 * pertains to some group of options
 * 
 * @author rvesse
 *
 */
public class ParseOptionGroupException extends ParseRestrictionViolatedException {
    private static final long serialVersionUID = 3018628261472277344L;
    
    private final Set<OptionMetadata> options;
    private final String tag;

    public ParseOptionGroupException(String message, String tag, Collection<OptionMetadata> options, Object... args) {
        super(message, args);
        this.options = AirlineUtils.unmodifiableSetCopy(options);
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public Set<OptionMetadata> getOptions() {
        return this.options;
    }

}