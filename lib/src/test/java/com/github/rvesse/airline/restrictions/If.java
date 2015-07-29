package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.RequiredOnlyIf;

@Command(name = "if")
public class If {

    @Option(name = { "-a", "--alpha" })
    public String a;
    
    @Option(name = { "-b", "--bravo" })
    @RequiredOnlyIf(names = { "-a", "--alpha" })
    public String b;
}
