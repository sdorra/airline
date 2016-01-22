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

import java.io.IOException;
import java.io.Writer;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.ControlTracker;

public class WriterControlTracker<T> extends ControlTracker<T> {

    private final Writer writer;

    public WriterControlTracker(Writer writer, ControlCodeSource<T> provider) {
        super(provider);
        if (writer == null)
            throw new NullPointerException("writer cannot be null");
        this.writer = writer;
    }

    @Override
    protected void resetInternal(T value) throws IOException {
        this.writer.write(this.provider.getResetControlCode(value));
    }

    @Override
    protected void applyInternal(T value) throws IOException {
        this.writer.write(this.provider.getControlCode(value));
    }

}
