package com.github.rvesse.airline;

import com.github.rvesse.airline.Arguments;
import com.github.rvesse.airline.Command;

@Command(name = "ArgsRequiredWrongMain")
public class ArgsRequiredWrongMain
{
    @Arguments(required = true)
    public String[] file;
}
