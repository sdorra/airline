package com.github.rvesse.airline.restrictions.ranges;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.ranges.DoubleRange;

@Command(name = "OptionDoubleRange")
public class OptionDoubleRangeInclusive extends OptionRangeBase {
    
    @Option(name = "-d", title = "Double")
    @DoubleRange(min = 0.0d, minInclusive = true, max = 1.0d, maxInclusive = true)
    public double d;
}
