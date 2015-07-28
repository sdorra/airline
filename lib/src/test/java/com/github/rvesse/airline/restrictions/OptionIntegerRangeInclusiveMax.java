package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.ranges.IntegerRange;

@Command(name = "OptionIntegerRange")
public class OptionIntegerRangeInclusiveMax extends OptionInteger {
    
    @Option(name = "-i", title = "Integer")
    @IntegerRange(min = 0, minInclusive = false, max = 100, maxInclusive = true)
    public int i;
}