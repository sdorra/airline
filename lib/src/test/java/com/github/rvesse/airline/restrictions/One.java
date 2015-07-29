package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.RequireOnlyOne;

@Command(name = "one")
public class One {
    
    @Option(name = "-a")
    @RequireOnlyOne(tag = "group")
    public String a;
    
    @Option(name = "-b")
    @RequireOnlyOne(tag = "group")
    public String b;
    
    @Option(name = "-c")
    @RequireOnlyOne(tag = "group")
    public String c;
}