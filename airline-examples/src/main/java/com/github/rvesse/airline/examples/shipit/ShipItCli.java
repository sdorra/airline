/**
 * Copyright (C) 2010-16 the original author or authors.
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
package com.github.rvesse.airline.examples.shipit;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Parser;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.examples.cli.commands.Help;
import com.github.rvesse.airline.parser.options.ListValueOptionParser;

//@formatter:off
@Cli(name = "ship-it", 
     defaultCommand = Help.class, 
     commands = {
             CheckAddress.class,
             Send.class,
             Price.class,
             Help.class
     },
     description = "A demonstration CLI around shipping",
     parserConfiguration = @Parser(
       useDefaultOptionParsers = true,
       defaultParsersFirst = false,
       optionParsers = { ListValueOptionParser.class }
     )
)
//@formatter:on
public class ShipItCli {

    public static void main(String[] args) {
        ExampleExecutor.executeCli(new com.github.rvesse.airline.Cli<ExampleRunnable>(ShipItCli.class), args);
    }
}
