package com.github.rvesse.airline.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class ControlOutputStream extends FilterOutputStream {

    private final List<OutputStreamControlTracker> controls = new ArrayList<OutputStreamControlTracker>();
    
    private static final byte[] NEWLINE_BYTES = "\n".getBytes();
    
    public ControlOutputStream(OutputStream output) {
        super(output);
    }
    
    protected final void registerControl(OutputStreamControlTracker control) {
        this.controls.add(control);
    }

    @Override
    public void write(int b) throws IOException {
        this.applyAll();
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.applyAll();
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.applyAll();
        super.write(b, off, len);
    }
    
    protected final void applyAll() throws IOException {
        for (OutputStreamControlTracker control : this.controls) {
            control.apply();
        }
    }

    @Override
    public void close() throws IOException {
        resetAll();
        super.close();
    }

    protected final void resetAll() throws IOException {
        for (OutputStreamControlTracker control : this.controls) {
            control.reset();
        }
    }
    
    public void print(String value) throws IOException {
        this.write(value.getBytes());
    }
    
    public void println(String value) throws IOException {
        this.write(value.getBytes());
        this.write(NEWLINE_BYTES);
    }
}
