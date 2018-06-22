package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsNoArguments")
public class ArgsNoArguments {

    @Option(name = { "-f", "--flag" })
    public boolean flag = false;
}
