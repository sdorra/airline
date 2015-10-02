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

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Group;
import com.github.rvesse.airline.annotations.Groups;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.OptionType;

@Groups({
        @Group(name = "groupInsideOfGroups subGroup", description = "my nested sub-group", defaultCommand = CommandWithSubGroupsAnnotation.class,commands = {CommandAdd.class}),
        @Group(name = "groupInsideOfGroups", description = "top level group", commands = { CommandWithSubGroupsAnnotation.class })
})
@Command(name = "commandWithSubGroupsAnno", description = "A command with a groups annotation defining a sub-group")
public class CommandWithSubGroupsAnnotation extends AbstractGroupAnnotationCommand
{
    
    @Option(name = "-v", type = OptionType.GROUP)
    public boolean verbose = false;
    
}
