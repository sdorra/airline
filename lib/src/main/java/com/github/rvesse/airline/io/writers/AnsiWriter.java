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
