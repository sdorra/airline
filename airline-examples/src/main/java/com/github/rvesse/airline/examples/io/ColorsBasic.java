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
package com.github.rvesse.airline.examples.io;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.io.colors.BasicColor;
import com.github.rvesse.airline.io.output.AnsiBasicColorizedOutputStream;
import com.github.rvesse.airline.io.output.ColorizedOutputStream;

@Command(name = "colors-basic", description = "Displays some text in ANSI basic colors (if your terminal supports this)")
public class ColorsBasic extends ColorDemo<BasicColor> implements ExampleRunnable {

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(ColorsBasic.class, args);
    }

    @Override
    protected ColorizedOutputStream<BasicColor> openOutputStream() {
        return new AnsiBasicColorizedOutputStream(System.out);
    }

    @Override
    protected BasicColor[] getColors() {
        return BasicColor.values();
    }
}
