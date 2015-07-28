package com.github.rvesse.airline.restrictions.ranges;

import com.github.rvesse.airline.annotations.Option;

public class OptionRangeBase {

    @Option(name = "-i", title = "Integer", arity = 1)
    public long i;
    
    @Option(name = "-d", title = "Double", arity = 1)
    public double d;

}