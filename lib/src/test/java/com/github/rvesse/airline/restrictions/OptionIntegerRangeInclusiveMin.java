package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.IntegerRange;

@Command(name = "OptionIntegerRange")
public class OptionIntegerRangeInclusiveMin extends OptionInteger {
    
    @Option(name = "-i", title = "Integer")
    @IntegerRange(min = 0, minInclusive = true, max = 100, maxInclusive = false)
    public int i;
}
