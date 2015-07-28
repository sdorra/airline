package com.github.rvesse.airline.restrictions.ranges;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.ranges.LongRange;

@Command(name = "OptionIntegerRange")
public class OptionIntegerRangeInclusiveMin extends OptionRangeBase {
    
    @Option(name = "-i", title = "Integer")
    @LongRange(min = 0, minInclusive = true, max = 100, maxInclusive = false)
    public long i;
}
