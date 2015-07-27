package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "Arity1")
public class Arity1
{
    @Option(arity = 1, name = "-inspect", description = "")
    public boolean inspect;
}
