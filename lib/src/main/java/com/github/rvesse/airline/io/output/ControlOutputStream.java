package com.github.rvesse.airline.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class ControlOutputStream extends PrintStream {

    private final List<OutputStreamControlTracker> controls = new ArrayList<OutputStreamControlTracker>();

    public ControlOutputStream(OutputStream output) {
        super(output);
    }

    protected final void registerControl(OutputStreamControlTracker control) {
        this.controls.add(control);
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

    protected final void applyAll() {
        try {
            for (OutputStreamControlTracker control : this.controls) {
                control.apply();
            }
        } catch (IOException e) {
            this.setError();
        }
    }

    @Override
    public void close() {
        resetAll();
        super.close();
    }

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
