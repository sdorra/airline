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
package com.github.rvesse.airline.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * An output stream that supports customized output via ANSI control codes
 */
@SuppressWarnings("rawtypes")
public abstract class AnsiOutputStream extends PrintStream {

    private final List<OutputStreamControlTracker> controls = new ArrayList<OutputStreamControlTracker>();

    public AnsiOutputStream(OutputStream output) {
        super(output);
    }

    /**
     * Registers a control
     * <p>
     * This method can be useful if you wish to add additional controls beyond
     * those provided by a specific class derived from this abstract class.
     * </p>
     * 
     * @param control
     *            Control
     */
    public final void registerControl(OutputStreamControlTracker control) {
        if (control == null)
            return;
        this.controls.add(control);
    }

    /**
     * Registers some controls
     * 
     * @param controls
     *            Controls
     */
    public final void registerControls(OutputStreamControlTracker... controls) {
        if (controls == null)
            return;
        for (OutputStreamControlTracker control : controls) {
            registerControl(control);
        }
    }

    @Override
    public void write(int b) {
        this.applyAll();
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.applyAll();
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        this.applyAll();
        super.write(b, off, len);
    }

    /**
     * Method which applies any necessary controls to the stream
     */
    protected final void applyAll() {
        try {
            for (OutputStreamControlTracker control : this.controls) {
                control.apply();
            }
        } catch (IOException e) {
            this.setError();
        }
    }

    /**
     * Resets the stream to the default state i.e. disables all controls that
     * may previously have been applied such as colors, text decorations etc
     * 
     * @param full
     *            If true do a full graphics reset in addition to resetting the
     *            individual controls
     */
    public void reset(boolean full) {
        this.resetAll();
        if (full) {
            try {
                super.write(AnsiControlCodes.getGraphicsResetCode().getBytes());
            } catch (IOException e) {
                this.setError();
            }
        }
    }

    @Override
    public void close() {
        resetAll();
        super.close();
    }

    /**
     * Method which resets the state of any controls that have been previously
     * enabled and applied to the stream
     */
    protected final void resetAll() {
        try {
            for (OutputStreamControlTracker control : this.controls) {
                control.reset();
            }
        } catch (IOException e) {
            this.setError();
        }
    }
}
