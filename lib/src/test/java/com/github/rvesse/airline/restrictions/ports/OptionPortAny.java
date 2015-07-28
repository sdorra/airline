package com.github.rvesse.airline.restrictions.ports;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Port;
import com.github.rvesse.airline.annotations.restrictions.PortType;

@Command(name = "OptionPort")
public class OptionPortAny extends OptionPortBase {

    @Option(name = "-p", title = "Port")
    @Port(acceptablePorts = { PortType.ANY })
    public int port;
    
}
