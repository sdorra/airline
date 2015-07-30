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

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.decorations.BasicDecoration;
import com.github.rvesse.airline.io.decorations.sources.AnsiDecorationSource;

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
        if (foregroundColorSource == null)
            throw new NullPointerException("foregroundColorSource cannot be null");
        if (backgroundColorSource == null)
            throw new NullPointerException("backgroundColorSource cannot be null");
        
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
