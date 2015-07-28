package com.github.rvesse.airline.restrictions.ranges;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.ranges.LongRange;

@Command(name = "OptionIntegerRange")
public class OptionIntegerRangeInvalid extends OptionRangeBase {
    
    @Option(name = "-i", title = "Integer")
    @LongRange(min = 100, minInclusive = true, max = 0, maxInclusive = true)
    public long i;
}
