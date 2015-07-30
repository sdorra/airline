package com.github.rvesse.airline.restrictions.common;

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.rvesse.airline.help.sections.HelpFormat;
import com.github.rvesse.airline.help.sections.HelpHint;
import com.github.rvesse.airline.restrictions.AbstractRestriction;

public abstract class AbstractAllowedValuesRestriction extends AbstractRestriction implements HelpHint {

    protected final Set<String> rawValues = new LinkedHashSet<String>();
    private final boolean caseInsensitive;

    public AbstractAllowedValuesRestriction(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    protected static Set<Object> asObjects(Set<String> set) {
        Set<Object> newSet = new LinkedHashSet<Object>();
        for (String item : set) {
            newSet.add((Object) item);
        }
        return newSet;
    }

    @Override
    public String getPreamble() {
        return String.format("This options value is restricted to the following set of %s:",
                this.caseInsensitive ? "case insensitive values" : "values");
    }

    @Override
    public HelpFormat getFormat() {
        return HelpFormat.LIST;
    }

    @Override
    public int numContentBlocks() {
        return 1;
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        if (blockNumber != 0)
            throw new IndexOutOfBoundsException();
        return this.rawValues.toArray(new String[this.rawValues.size()]);
    }

}