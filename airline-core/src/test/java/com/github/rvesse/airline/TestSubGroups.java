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
package com.github.rvesse.airline;

import org.apache.commons.collections4.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.builder.GroupBuilder;
import com.github.rvesse.airline.help.Help;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.parser.errors.ParseCommandMissingException;
import com.github.rvesse.airline.utils.predicates.parser.GroupFinder;


public class TestSubGroups {

    @Test
    public void sub_groups_01() {
        //@formatter:off
        CliBuilder<Object> builder
            = Cli.<Object>builder("test");
        builder.withGroup("foo")
               .withSubGroup("bar")
               .withDefaultCommand(Help.class);
        //@formatter:on
        
        Cli<Object> cli = builder.build();
        GlobalMetadata<Object> global = cli.getMetadata();
        Assert.assertEquals(global.getDefaultGroupCommands().size(), 0);
        Assert.assertEquals(global.getCommandGroups().size(), 1);
        
        CommandGroupMetadata parentGroup = global.getCommandGroups().get(0);
        Assert.assertEquals(parentGroup.getName(), "foo");
        Assert.assertEquals(parentGroup.getCommands().size(), 0);
        Assert.assertEquals(parentGroup.getSubGroups().size(), 1);
        
        CommandGroupMetadata subGroup = parentGroup.getSubGroups().get(0);
        Assert.assertEquals(subGroup.getName(), "bar");
        Assert.assertEquals(subGroup.getDefaultCommand().getType(), Help.class);
        
        Object cmd = cli.parse("foo", "bar");
        Assert.assertTrue(cmd instanceof Help);
    }
    
    @Test
    public void sub_groups_02() {
        //@formatter:off
        CliBuilder<Object> builder
            = Cli.<Object>builder("test");
        GroupBuilder<Object> fooBuilder = builder.withGroup("foo");
        fooBuilder.withSubGroup("bar")
                  .withDefaultCommand(Help.class);
        fooBuilder.withSubGroup("baz");
        //@formatter:on
        
        Cli<Object> cli = builder.build();
        GlobalMetadata<Object> global = cli.getMetadata();
        Assert.assertEquals(global.getDefaultGroupCommands().size(), 0);
        Assert.assertEquals(global.getCommandGroups().size(), 1);
        
        CommandGroupMetadata parentGroup = global.getCommandGroups().get(0);
        Assert.assertEquals(parentGroup.getName(), "foo");
        Assert.assertEquals(parentGroup.getCommands().size(), 0);
        Assert.assertEquals(parentGroup.getSubGroups().size(), 2);
        
        CommandGroupMetadata subGroup = CollectionUtils.find(parentGroup.getSubGroups(), new GroupFinder("bar"));
        Assert.assertNotNull(subGroup);
        Assert.assertEquals(subGroup.getName(), "bar");
        Assert.assertEquals(subGroup.getCommands().size(), 1);
        Assert.assertEquals(subGroup.getDefaultCommand().getType(), Help.class);
        
        subGroup = CollectionUtils.find(parentGroup.getSubGroups(), new GroupFinder("baz"));
        Assert.assertNotNull(subGroup);
        Assert.assertEquals(subGroup.getName(), "baz");
        Assert.assertEquals(subGroup.getCommands().size(), 0);
        Assert.assertNull(subGroup.getDefaultCommand());
        
        Object cmd = cli.parse("foo", "bar");
        Assert.assertTrue(cmd instanceof Help);
    }
    
    @Test(expectedExceptions = ParseCommandMissingException.class)
    public void sub_groups_03() {
        //@formatter:off
        CliBuilder<Object> builder
            = Cli.<Object>builder("test");
        GroupBuilder<Object> fooBuilder = builder.withGroup("foo");
        fooBuilder.withSubGroup("bar")
                  .withDefaultCommand(Help.class);
        fooBuilder.withSubGroup("baz");
        //@formatter:on
        
        builder.build().parse("foo", "baz");
    }
}
