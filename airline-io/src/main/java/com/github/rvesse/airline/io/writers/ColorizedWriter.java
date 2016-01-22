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
import com.github.rvesse.airline.io.decorations.BasicDecoration;
import com.github.rvesse.airline.io.decorations.sources.AnsiDecorationSource;

/**
 * An writer stream that supports colorization and basic text decorations
 *
 * @param <T>
 *            Color type
 */
public class ColorizedWriter<T> extends AnsiWriter {

    protected WriterControlTracker<T> foregroundControl, backgroundControl;
    protected WriterControlTracker<BasicDecoration> bold, italic, underline, strikeThrough;

    public ColorizedWriter(Writer writer, ControlCodeSource<T> foregroundColorSource,
            ControlCodeSource<T> backgroundColorSource) {
        super(writer);
        if (foregroundColorSource == null)
            throw new NullPointerException("foregroundColorSource cannot be null");
        if (backgroundColorSource == null)
            throw new NullPointerException("backgroundColorSource cannot be null");

        this.foregroundControl = new WriterControlTracker<T>(writer, foregroundColorSource);
        this.backgroundControl = new WriterControlTracker<T>(writer, backgroundColorSource);
        this.registerControls(this.foregroundControl, this.backgroundControl);

        AnsiDecorationSource<BasicDecoration> decorationsSource = new AnsiDecorationSource<BasicDecoration>();
        this.bold = new WriterControlTracker<BasicDecoration>(writer, decorationsSource);
        this.italic = new WriterControlTracker<BasicDecoration>(writer, decorationsSource);
        this.underline = new WriterControlTracker<BasicDecoration>(writer, decorationsSource);
        this.strikeThrough = new WriterControlTracker<BasicDecoration>(writer, decorationsSource);
        this.registerControls(this.bold, this.italic, this.underline, this.strikeThrough);
    }

    public ColorizedWriter<T> setForegroundColor(T color) {
        this.foregroundControl.set(color);
        return this;
    }

    public ColorizedWriter<T> resetForegroundColor() throws IOException {
        this.foregroundControl.reset();
        return this;
    }

    public ColorizedWriter<T> setBackgroundColor(T color) {
        this.backgroundControl.set(color);
        return this;
    }

    public ColorizedWriter<T> resetBackgroundColor() throws IOException {
        this.backgroundControl.reset();
        return this;
    }

    public ColorizedWriter<T> setBold(boolean enabled) throws IOException {
        return setDecoration(enabled, BasicDecoration.BOLD, this.bold);
    }

    public ColorizedWriter<T> setItalic(boolean enabled) throws IOException {
        return setDecoration(enabled, BasicDecoration.ITALIC, this.italic);
    }

    public ColorizedWriter<T> setUnderline(boolean enabled) throws IOException {
        return setDecoration(enabled, BasicDecoration.UNDERLINE, this.underline);
    }

    public ColorizedWriter<T> setStrikeThrough(boolean enabled) throws IOException {
        return setDecoration(enabled, BasicDecoration.STRIKE_THROUGH, this.strikeThrough);
    }

    protected final ColorizedWriter<T> setDecoration(boolean enabled, BasicDecoration decoration,
            WriterControlTracker<BasicDecoration> control) throws IOException {
        if (enabled) {
            control.set(decoration);
        } else {
            control.reset();
        }
        return this;
    }
}
