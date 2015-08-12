package com.github.rvesse.airline.command;

import java.util.List;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Option;

public class AbstractGroupAnnotationCommand {

    @Arguments(description = "Patterns of files to be added")
    public List<String> patterns;
    @Option(name = "-i")
    public Boolean interactive = false;

    public AbstractGroupAnnotationCommand() {
        super();
    }

}