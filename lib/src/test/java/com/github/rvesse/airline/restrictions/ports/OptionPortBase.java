package com.github.rvesse.airline.restrictions.ports;

import com.github.rvesse.airline.annotations.Option;

public class OptionPortBase {

    @Option(name = "-p", title = "Port", arity = 1)
    public int port;
    
}
