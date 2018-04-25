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
package com.github.rvesse.airline.examples.cli.aliases;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.examples.ExampleRunnable;

@Command(name = "logs", description = "Show log information")
public class Logs implements ExampleRunnable {
    
    public static enum Format {
        PlainText,
        Json,
        Xml
    }
    
    @Option(name = { "-f", "--format" }, title = "Format", description = "Sets the desired output format")
    private Format format = Format.PlainText;

    @Override
    public int run() {
        // In a real command actual implementation would go here...
        System.out.println("Output Format: " + this.format.name());
        return 0;
    }
}
