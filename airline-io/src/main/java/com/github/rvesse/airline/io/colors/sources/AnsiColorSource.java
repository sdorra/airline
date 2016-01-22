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
package com.github.rvesse.airline.io.colors.sources;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.AnsiControlCodes;
import com.github.rvesse.airline.io.colors.AnsiColorProvider;

public abstract class AnsiColorSource<T extends AnsiColorProvider> implements ControlCodeSource<T> {

    private final boolean foreground;

    public AnsiColorSource() {
        this(true);
    }

    protected AnsiColorSource(boolean foreground) {
        this.foreground = foreground;
    }

    @Override
    public String getControlCode(T attributeSource) {
        return this.foreground ? attributeSource.getAnsiForegroundControlCode() : attributeSource
                .getAnsiBackgroundControlCode();
    }

    @Override
    public String getResetControlCode(T attributeSource) {
        if (attributeSource.usesExtendedColors())
            return getFullResetControlCode();

        StringBuilder builder = new StringBuilder();
        builder.append(AnsiControlCodes.ESCAPE);
        builder.append(this.foreground ? AnsiControlCodes.DEFAULT_FOREGROUND : AnsiControlCodes.DEFAULT_BACKGROUND);
        builder.append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        return builder.toString();
    }

    public String getFullResetControlCode() {
        return AnsiControlCodes.getGraphicsResetCode();
    }
}
