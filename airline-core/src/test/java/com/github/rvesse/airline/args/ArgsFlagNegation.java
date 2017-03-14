package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsFlagNegation")
public class ArgsFlagNegation {

    @Option(name = { "--false", "--no-false" })
    public boolean falseFlag = false;
    
    @Option(name = { "--true", "--no-true" })
    public boolean trueFlag = true;
}
