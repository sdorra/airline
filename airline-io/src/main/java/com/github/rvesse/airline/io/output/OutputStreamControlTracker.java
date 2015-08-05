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
package com.github.rvesse.airline.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.ControlTracker;

public class OutputStreamControlTracker<T> extends ControlTracker<T> {

    private final OutputStream output;
    private final Charset charset;

    public OutputStreamControlTracker(OutputStream output, ControlCodeSource<T> provider) {
        this(output, null, provider);
    }

    public OutputStreamControlTracker(OutputStream output, Charset charset, ControlCodeSource<T> provider) {
        super(provider);
        if (output == null)
            throw new NullPointerException("output cannot be null");
        this.output = output;
        this.charset = charset;
    }

    @Override
    protected void resetInternal(T value) throws IOException {
        String code = this.provider.getResetControlCode(value);
        this.output.write(getBytes(code));
    }

    private byte[] getBytes(String code) {
        return this.charset != null ? code.getBytes(this.charset) : code.getBytes();
    }

    @Override
    protected void applyInternal(T value) throws IOException {
        String code = this.provider.getControlCode(value);
        this.output.write(getBytes(code));
    }

}
