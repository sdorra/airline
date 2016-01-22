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
package com.github.rvesse.airline.io.writers;

import java.io.Writer;

import com.github.rvesse.airline.io.colors.Color256;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized writer supporting the ANSI 256 colour palette
 * @author rvesse
 *
 */
public class Ansi256ColorizedWriter extends ColorizedWriter<Color256> {

    public Ansi256ColorizedWriter(Writer writer) {
        super(writer, new AnsiForegroundColorSource<Color256>(), new AnsiBackgroundColorSource<Color256>());
    }

}
