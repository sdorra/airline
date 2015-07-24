package com.github.rvesse.airline;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;

@Command(name = "ArgsRequiredWrongMain")
public class ArgsRequiredWrongMain
{
    @Arguments(required = true)
    public String[] file;
}
