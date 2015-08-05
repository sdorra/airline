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
package com.github.rvesse.airline.examples.simple;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * A command that has some required options
 *
 */
@Command(name = "required", description = "A command with required options")
public class Required implements ExampleRunnable {
    
    @Option(name = "--required", title = "Value", arity = 1, description = "A required option")
    private String required;
    
    @Option(name = "--optional", title = "Value", arity = 1, description = "An optional option")
    private String optional;
    
    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Required.class, args);
    }

    @Override
    public int run() {
        System.out.println("Required value given was " + this.required);
        System.out.println("Optional value was " + this.optional);
        return 0;
    }

}
