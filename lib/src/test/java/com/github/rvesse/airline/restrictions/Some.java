package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.RequireSome;

@Command(name = "some")
public class Some {
    
    @Option(name = "-a")
    @RequireSome(tag = "group")
    public String a;
    
    @Option(name = "-b")
    @RequireSome(tag = "group")
    public String b;
    
    @Option(name = "-c")
    @RequireSome(tag = "group")
    public String c;
}