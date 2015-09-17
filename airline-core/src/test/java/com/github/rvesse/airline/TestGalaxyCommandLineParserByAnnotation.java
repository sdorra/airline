package com.github.rvesse.airline;

import org.testng.annotations.Test;

import com.github.rvesse.airline.TestGalaxyCommandLineParser.AgentAddCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.AgentShowCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.AgentTerminateCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.HelpCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.InstallCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.ResetToActualCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.RestartCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.ShowCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.SshCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.StartCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.StopCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.TerminateCommand;
import com.github.rvesse.airline.TestGalaxyCommandLineParser.UpgradeCommand;
import com.github.rvesse.airline.annotations.Group;

@Test
//@formatter:off
@com.github.rvesse.airline.annotations.Cli(
    name = "galaxy", 
    description = "cloud management system", 
    defaultCommand = HelpCommand.class, 
    commands = {
        HelpCommand.class, 
        ShowCommand.class, 
        InstallCommand.class, 
        UpgradeCommand.class, 
        TerminateCommand.class,
        StartCommand.class, 
        StopCommand.class, 
        RestartCommand.class, 
        SshCommand.class,
        ResetToActualCommand.class 
    }, 
    groups = {
        @Group(name = "agent", 
               description = "Manage agents", 
               defaultCommand = AgentShowCommand.class, 
               commands = {
                   AgentShowCommand.class, 
                   AgentAddCommand.class, 
                   AgentTerminateCommand.class 
               }) 
    })
//@formatter:on
public class TestGalaxyCommandLineParserByAnnotation extends TestGalaxyCommandLineParser {

    @Override
    protected Cli<GalaxyCommand> createParser() {
        return new Cli<GalaxyCommand>(TestGalaxyCommandLineParserByAnnotation.class);
    }

}
