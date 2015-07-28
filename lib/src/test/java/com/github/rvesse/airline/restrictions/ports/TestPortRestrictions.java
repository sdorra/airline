package com.github.rvesse.airline.restrictions.ports;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.annotations.restrictions.PortType;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.PortRestriction;

public class TestPortRestrictions {
    
    private void hasPortRestriction(CommandMetadata metadata) {
        for (OptionMetadata option : metadata.getAllOptions()) {
            for (OptionRestriction restriction : option.getRestrictions()) {
                if (restriction instanceof PortRestriction) return;
            }
        }
        Assert.fail("No PortRestriction found");
    }

    @Test
    public void port_any() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAny.class);
        hasPortRestriction(parser.getCommandMetadata());
        
        for (int i = 0; i <= 65535; i++) {
            OptionPortBase cmd = parser.parse("-p", Integer.toString(i));
            Assert.assertEquals(cmd.port, i);
        }
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_any_below_min() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAny.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", "-1");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_any_above_max() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAny.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", "65536");
    }
    
    @Test
    public void port_ephemeral() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortEphemeral.class);
        hasPortRestriction(parser.getCommandMetadata());
        
        for (int i = PortType.DYNAMIC.getMinimumPort(); i <= PortType.DYNAMIC.getMinimumPort(); i++) {
            OptionPortBase cmd = parser.parse("-p", Integer.toString(i));
            Assert.assertEquals(cmd.port, i);
        }
        OptionPortBase cmd = parser.parse("-p", "0");
        Assert.assertEquals(cmd.port, 0);
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_ephemeral_bad_system() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortEphemeral.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", Integer.toString(PortType.SYSTEM.getMinimumPort()));
    }
}
