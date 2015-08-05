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
package com.github.rvesse.airline.command;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.errors.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.utils.AirlineUtils;

public class CommandGroupAnnotationTest {
    /*
     * Tests for Groups -> Group annotations
     */
    @Test
    public void groupIsCreatedFromGroupsAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupsAnnotation.class).build();

        Object command = parser.parse("groupInsideOfGroups", "commandWithGroupsAnno", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandWithGroupsAnnotation);
        CommandWithGroupsAnnotation add = (CommandWithGroupsAnnotation) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    @Test
    public void extraCommandsAreAddedFromGroupsAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupsAnnotation.class).build();

        Object command = parser.parse("groupInsideOfGroups", "add", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandAdd);
        CommandAdd add = (CommandAdd) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    @Test(expectedExceptions = ParseCommandUnrecognizedException.class)
    public void commandRemovedFromDefaultGroupWithGroupsAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupsAnnotation.class).build();

        parser.parse("commandWithGroupsAnno", "-i", "A.java");

    }

    /*
     * Tests for Groups -> Group annotations
     */
    @Test
    public void groupOptionsAreAddedFromGroupsAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupsAnnotation.class).build();
        Assert.assertEquals(parser.getMetadata().getCommandGroups().size(), 1);
        CommandGroupMetadata group = parser.getMetadata().getCommandGroups().get(0);
        Assert.assertEquals(group.getOptions().size(), 1);
        OptionMetadata option = group.getOptions().get(0);
        Assert.assertEquals("-v", AirlineUtils.first(option.getOptions()));
    }

    /*
     * Note: Disabling this test for now because there's a bug when the parser
     * parses but doesn't find a command. It then properly uses the
     * defaultCommand, however the input is still marked as unparsed which
     * causes the default command to throw an exception. We need to fix the
     * parser/CLI to re-parse the input after calling
     * state.withCommand(defaultCommand)
     */
    @Test
    public void defaultCommandIsAddedFromGroupsAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupsAnnotation.class).build();

        Object command = parser.parse("groupInsideOfGroups", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandWithGroupsAnnotation);
        CommandWithGroupsAnnotation add = (CommandWithGroupsAnnotation) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    /*
     * Tests for Group annotation
     */
    @Test
    public void groupIsCreatedFromGroupAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupAnnotation.class).build();

        Object command = parser.parse("singleGroup", "commandWithGroup", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandWithGroupAnnotation);
        CommandWithGroupAnnotation add = (CommandWithGroupAnnotation) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    @Test
    public void extraCommandsAreAddedFromGroupAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupAnnotation.class).build();

        Object command = parser.parse("singleGroup", "add", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandAdd);
        CommandAdd add = (CommandAdd) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    @Test(expectedExceptions = ParseCommandUnrecognizedException.class)
    public void commandRemovedFromDefaultGroupWithGroupAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupAnnotation.class).build();

        parser.parse("commandWithGroup", "-i", "A.java");

    }

    /*
     * Note: Disabling this test for now because there's a bug when the parser
     * parses but doesn't find a command. It then properly uses the
     * defaultCommand, however the input is still marked as unparsed which
     * causes the default command to throw an exception. We need to fix the
     * parser/CLI to re-parse the input after calling
     * state.withCommand(defaultCommand)
     */
    @Test
    public void defaultCommandIsAddedFromGroupAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupAnnotation.class).build();

        Object command = parser.parse("singleGroup", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandWithGroupAnnotation);
        CommandWithGroupAnnotation add = (CommandWithGroupAnnotation) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    /*
     * Tests for groupNames in Command annotation
     */
    @SuppressWarnings("unchecked")
    @Test
    public void addedToGroupFromGroupAnnotation() {
        Cli<?> parser = Cli.builder("junk").withCommands(CommandWithGroupAnnotation.class, CommandWithGroupNames.class)
                .build();

        Object command = parser.parse("singleGroup", "commandWithGroupNames", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandWithGroupNames);
        CommandWithGroupNames add = (CommandWithGroupNames) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    @Test
    public void addedToSingletonGroupWithoutGroupAnnotation() {
        @SuppressWarnings("unchecked")
        Cli<?> parser = Cli.builder("junk").withCommands(CommandWithGroupNames.class).build();

        Object command = parser.parse("singletonGroup", "commandWithGroupNames", "-i", "A.java");
        Assert.assertNotNull(command, "command is null");
        Assert.assertTrue(command instanceof CommandWithGroupNames);
        CommandWithGroupNames add = (CommandWithGroupNames) command;
        Assert.assertEquals(add.interactive.booleanValue(), true);
        Assert.assertEquals(add.patterns, Arrays.asList("A.java"));
    }

    @Test(expectedExceptions = ParseCommandUnrecognizedException.class)
    public void commandRemovedFromDefaultGroupWithGroupNames() {
        Cli<?> parser = Cli.builder("junk").withCommand(CommandWithGroupNames.class).build();

        parser.parse("commandWithGroupNames", "-i", "A.java");

    }

}
