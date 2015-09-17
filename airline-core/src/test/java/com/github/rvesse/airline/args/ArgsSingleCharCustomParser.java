package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Parser;
import com.github.rvesse.airline.parser.options.StandardOptionParser;

@Command(name = "ArgsSingleCharCustomParser")
@Parser(optionParsers = { StandardOptionParser.class }, useDefaultOptionParsers = false)
public class ArgsSingleCharCustomParser extends ArgsSingleChar {

}
