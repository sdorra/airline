/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.restrictions.ports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.annotations.restrictions.PortType;
import com.github.rvesse.airline.help.Help;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.PortRestriction;

public class TestPortRestrictions {

    private void hasPortRestriction(CommandMetadata metadata) {
        for (OptionMetadata option : metadata.getAllOptions()) {
            for (OptionRestriction restriction : option.getRestrictions()) {
                if (restriction instanceof PortRestriction)
                    return;
            }
        }
        Assert.fail("No PortRestriction found");
    }

    private void checkPorts(SingleCommand<? extends OptionPortBase> parser, PortType type) {
        checkPorts(parser, type.getMinimumPort(), type.getMaximumPort());
    }

    private void checkPorts(SingleCommand<? extends OptionPortBase> parser, int min, int max) {
        for (int i = min; i <= max; i++) {
            OptionPortBase cmd = parser.parse("-p", Integer.toString(i));
            Assert.assertEquals(cmd.port, i);
        }
    }
    
    private String checkHelp(SingleCommand<? extends OptionPortBase> parser, PortType[] included) throws IOException {
        return checkHelp(parser, included, new PortType[0]);
    }

    private String checkHelp(SingleCommand<? extends OptionPortBase> parser, PortType[] included, PortType[] excluded)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Help.help(parser.getCommandMetadata(), output);
        String usage = new String(output.toByteArray(), StandardCharsets.UTF_8);

        for (PortType range : included) {
            if (range.getMinimumPort() != range.getMaximumPort()) {
                Assert.assertTrue(
                        usage.contains(String.format("%d-%d", range.getMinimumPort(), range.getMaximumPort())));
            } else {
                Assert.assertTrue(usage.contains(String.format("%d", range.getMinimumPort())));
            }
        }
        for (PortType range : excluded) {
            if (range.getMinimumPort() != range.getMaximumPort()) {
                Assert.assertFalse(
                        usage.contains(String.format("%d-%d", range.getMinimumPort(), range.getMaximumPort())));
            } else {
                Assert.assertFalse(usage.contains(String.format("%d", range.getMinimumPort())));
            }
        }
        
        return usage;
    }

    @Test
    public void port_any() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAny.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkPorts(parser, PortType.ANY);
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
    public void port_all() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkPorts(parser, PortType.ANY);
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_all_below_min() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", "-1");
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_all_above_max() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", "65536");
    }
    
    @Test
    public void port_all2() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll2.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkPorts(parser, PortType.ANY);
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_all2_below_min() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll2.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", "-1");
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_all2_above_max() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll2.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", "65536");
    }

    @Test
    public void port_ephemeral() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortEphemeral.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkPorts(parser, PortType.DYNAMIC);
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_ephemeral_bad_system() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortEphemeral.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", Integer.toString(PortType.SYSTEM.getMinimumPort()));
    }

    @Test
    public void port_several() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortSeveral.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkPorts(parser, PortType.SYSTEM);
        checkPorts(parser, PortType.DYNAMIC);
    }

    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void port_several_user() {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortSeveral.class);
        hasPortRestriction(parser.getCommandMetadata());
        parser.parse("-p", Integer.toString(PortType.USER.getMinimumPort()));
    }

    @Test
    public void port_help_any() throws IOException {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAny.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkHelp(parser, new PortType[] { PortType.ANY });
    }

    @Test
    public void port_help_ephermeral() throws IOException {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortEphemeral.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkHelp(parser, new PortType[] { PortType.DYNAMIC });
    }
    
    @Test
    public void port_help_several() throws IOException {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortSeveral.class);
        hasPortRestriction(parser.getCommandMetadata());

        String usage = checkHelp(parser, new PortType[] { PortType.DYNAMIC, PortType.SYSTEM });
        // Check that port ranges are ordered appropriately
        Assert.assertTrue(usage.contains("1-1023, 49152-65535"));
    }
    
    @Test
    public void port_help_all() throws IOException {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkHelp(parser, new PortType[] { PortType.ANY }, new PortType[] { PortType.SYSTEM, PortType.USER, PortType.DYNAMIC });
    }
    
    @Test
    public void port_help_all2() throws IOException {
        SingleCommand<? extends OptionPortBase> parser = TestingUtil.singleCommandParser(OptionPortAll2.class);
        hasPortRestriction(parser.getCommandMetadata());

        checkHelp(parser, new PortType[] { PortType.ANY }, new PortType[] { PortType.SYSTEM, PortType.USER, PortType.DYNAMIC });
    }
}
