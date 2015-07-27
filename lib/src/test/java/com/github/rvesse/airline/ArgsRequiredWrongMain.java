package com.github.rvesse.airline;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "ArgsRequiredWrongMain")
public class ArgsRequiredWrongMain {
    @Arguments
    @Required
    public String[] file;
}
