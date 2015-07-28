package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.ranges.IntegerRange;

@Command(name = "OptionIntegerRange")
public class OptionIntegerRangeSingleValue extends OptionInteger {
    
    @Option(name = "-i", title = "Integer")
    @IntegerRange(min = 0, minInclusive = true, max = 0, maxInclusive = true)
    public int i;
}