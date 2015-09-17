package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Parser;
import com.github.rvesse.airline.parser.options.StandardOptionParser;

@Command(name = "Args1CustomParser", description = "args1 description")
@Parser(allowOptionAbbreviation = true, optionParsers = { StandardOptionParser.class })
public class Args1CustomParser extends Args1 {

}
