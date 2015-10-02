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
package com.github.rvesse.airline.io.writers;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * A writer that supports customizing the output with ANSI control codes
 * 
 * @author rvesse
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AnsiWriter extends FilterWriter {

    private final List<WriterControlTracker> controls = new ArrayList<WriterControlTracker>();

    public AnsiWriter(Writer writer) {
        super(writer);
    }

    public final void registerControl(WriterControlTracker control) {
        if (control == null)
            return;
        this.controls.add(control);
    }

    public final void registerControls(WriterControlTracker... controls) {
        if (controls == null)
            return;
        for (WriterControlTracker control : controls) {
            registerControl(control);
        }
    }

    @Override
    public void write(int c) throws IOException {
        this.applyAll();
        super.write(c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.applyAll();
        super.write(cbuf, off, len);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        this.applyAll();
        super.write(str, off, len);
    }

    protected final void applyAll() throws IOException {
        for (WriterControlTracker control : this.controls) {
            control.apply();
        }
    }

    @Override
    public void close() throws IOException {
        resetAll();
        super.close();
    }

    protected final void resetAll() throws IOException {
        for (WriterControlTracker control : this.controls) {
            control.reset();
        }
    }
}
