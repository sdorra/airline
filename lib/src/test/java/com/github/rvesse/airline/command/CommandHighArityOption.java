package com.github.rvesse.airline.command;

import java.util.List;

import javax.inject.Inject;

import com.github.rvesse.airline.Arguments;
import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;

@Command(name = "cmd", description = "A command with an option that has a high arity option")
public class CommandHighArityOption {
	@Inject
	public CommandMain commandMain;
	
	@Option(name = "--option", description = "An option with high arity", arity = Integer.MAX_VALUE)
	public List<String> option;
	
	@Option(name = "--option2", description = "Just another option")
	public String option2;
	
	@Arguments(description = "The rest of arguments")
	public List<String> args;
}
