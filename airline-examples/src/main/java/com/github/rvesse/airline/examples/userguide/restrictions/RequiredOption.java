package com.github.rvesse.airline.examples.userguide.restrictions;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "required")
public class RequiredOption {

    @Option(name = "--name", arity = 1, title = "Name")
    @Required
    private String name;
    
    public static void main(String[] args) {
        SingleCommand.singleCommand(RequiredOption.class).parse(args);
    }
}
