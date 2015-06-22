package com.github.rvesse.airline.io.output;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.decorations.BasicDecoration;
import com.github.rvesse.airline.io.decorations.sources.AnsiDecorationSource;
import com.google.common.base.Preconditions;

/**
 * An output stream that supports colorization and some basic text decorations
 * 
 * @author rvesse
 *
 * @param <T>
 *            Color type
 */
public class ColorizedOutputStream<T> extends AnsiOutputStream {

    protected OutputStreamControlTracker<T> foregroundControl, backgroundControl;
    protected OutputStreamControlTracker<BasicDecoration> bold, italic, underline, strikeThrough;

    public ColorizedOutputStream(OutputStream output, ControlCodeSource<T> foregroundColorSource,
            ControlCodeSource<T> backgroundColorSource) {
        super(output);
        Preconditions.checkNotNull(foregroundColorSource);
        Preconditions.checkNotNull(backgroundColorSource);
        this.foregroundControl = new OutputStreamControlTracker<T>(output, foregroundColorSource);
        this.backgroundControl = new OutputStreamControlTracker<T>(output, backgroundColorSource);
        this.registerControls(this.foregroundControl, this.backgroundControl);

        AnsiDecorationSource<BasicDecoration> decorationsSource = new AnsiDecorationSource<BasicDecoration>();
        this.bold = new OutputStreamControlTracker<BasicDecoration>(output, decorationsSource);
        this.italic = new OutputStreamControlTracker<BasicDecoration>(output, decorationsSource);
        this.underline = new OutputStreamControlTracker<BasicDecoration>(output, decorationsSource);
        this.strikeThrough = new OutputStreamControlTracker<BasicDecoration>(output, decorationsSource);
        this.registerControls(this.bold, this.italic, this.underline, this.strikeThrough);
    }

    public ColorizedOutputStream<T> setForegroundColor(T color) {
        this.foregroundControl.set(color);
        return this;
    }

    public ColorizedOutputStream<T> resetForegroundColor() {
        try {
            this.foregroundControl.reset();
        } catch (IOException e) {
            this.setError();
        }
        return this;
    }

    public ColorizedOutputStream<T> setBackgroundColor(T color) {
        this.backgroundControl.set(color);
        return this;
    }

    public ColorizedOutputStream<T> resetBackgroundColor() {
        try {
            this.backgroundControl.reset();
        } catch (IOException e) {
            this.setError();
        }
        return this;
    }

    public ColorizedOutputStream<T> setBold(boolean enabled) {
        setDecoration(enabled, BasicDecoration.BOLD, this.bold);
        return this;
    }
    
    public ColorizedOutputStream<T> setItalic(boolean enabled) {
        setDecoration(enabled, BasicDecoration.ITALIC, this.italic);
        return this;
    }
    
    public ColorizedOutputStream<T> setUnderline(boolean enabled) {
        setDecoration(enabled, BasicDecoration.UNDERLINE, this.underline);
        return this;
    }
    
    public ColorizedOutputStream<T> setStrikeThrough(boolean enabled) {
        setDecoration(enabled, BasicDecoration.STRIKE_THROUGH, this.strikeThrough);
        return this;
    }
    
    protected final void setDecoration(boolean enabled, BasicDecoration decoration, OutputStreamControlTracker<BasicDecoration> control) {
        try {
            if (enabled) {
                control.set(decoration);
            } else {
                control.reset();
            }
        } catch (IOException e) {
            this.setError();
        }
    }
}
