package com.github.rvesse.airline.restrictions.ports;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Port;
import com.github.rvesse.airline.annotations.restrictions.PortType;

@Command(name = "OptionPort")
public class OptionPortEphemeral extends OptionPortBase {

    @Option(name = "-p", title = "Port")
    @Port(acceptablePorts = { PortType.OS_ALLOCATED, PortType.DYNAMIC })
    public int port;
    
}
