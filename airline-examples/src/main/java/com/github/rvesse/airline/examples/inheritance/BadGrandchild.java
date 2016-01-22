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
package com.github.rvesse.airline.examples.inheritance;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;

/**
 * When inheriting from existing classes the default behaviour is to prevent
 * overriding of options as Airline assumes the conflicting definitions are an
 * error on the part of the developer. See {@link GoodGrandchild} for an example
 * of how to do option overrides correctly.
 *
 */
@Command(name = "bad-grandchild", description = "An illegal command which attempts to overrides an option defined by a parent without explicitly declaring the override")
public class BadGrandchild extends Child {

    /**
     * Trying to override the option here will fail because we didn't explicitly
     * state we were overriding
     */
    @Option(name = "--parent", description = "An option can be overridden if we are explicit about it")
    private boolean parent;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(BadGrandchild.class, args);
    }
}
