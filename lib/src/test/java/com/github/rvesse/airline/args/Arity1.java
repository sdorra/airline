package com.github.rvesse.airline.args;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;

@Command(name = "Arity1")
public class Arity1
{
    @Option(arity = 1, name = "-inspect", description = "", required = false)
    public boolean inspect;
}
