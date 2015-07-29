package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.MaxOccurrences;
import com.github.rvesse.airline.annotations.restrictions.MinOccurrences;
import com.github.rvesse.airline.annotations.restrictions.Once;

@Command(name = "occurrences")
public class Occurrences {

    @Option(name = "-a")
    @MaxOccurrences(occurrences = 3)
    public String a;
    
    @Option(name = "-b")
    @MinOccurrences(occurrences = 2)
    public String b;
    
    @Option(name = "-c")
    @Once
    public String c;
}
