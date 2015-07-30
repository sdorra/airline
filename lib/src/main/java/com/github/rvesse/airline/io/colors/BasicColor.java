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
package com.github.rvesse.airline.io.colors;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * The 8 basic ANSI Colours
 */
public enum BasicColor implements AnsiColorProvider {
    //@formatter:off
    BLACK(0, "Black"), 
    RED(1, "Red"), 
    GREEN(2, "Green"), 
    YELLOW(3, "Yellow"), 
    BLUE(4, "Blue"), 
    MAGENTA(5, "Magenta"), 
    CYAN(6, "Cyan"), 
    WHITE(7, "White"),
    BRIGHT_BLACK(0, "Black", true), 
    BRIGHT_RED(1, "Red", true), 
    BRIGHT_GREEN(2, "Green", true), 
    BRIGHT_YELLOW(3, "Yellow", true), 
    BRIGHT_BLUE(4, "Blue", true), 
    BRIGHT_MAGENTA(5, "Magenta", true), 
    BRIGHT_CYAN(6, "Cyan", true), 
    BRIGHT_WHITE(7, "White", true);
    //@formatter:on

    private final int index;
    private final String name;
    private final boolean highIntensity;

    BasicColor(int index, String name) {
        this(index, name, false);
    }

    BasicColor(int index, String name, boolean highIntensity) {
        this.index = index;
        this.name = name;
        this.highIntensity = highIntensity;
    }

    @Override
    public String getAnsiForegroundControlCode() {
        return getAnsiControlCode(this.highIntensity ? AnsiControlCodes.FOREGROUND_BRIGHT : AnsiControlCodes.FOREGROUND);
    }

    @Override
    public String getAnsiBackgroundControlCode() {
        return getAnsiControlCode(this.highIntensity ? AnsiControlCodes.BACKGROUND_BRIGHT : AnsiControlCodes.BACKGROUND);
    }

    @Override
    public boolean usesExtendedColors() {
        return false;
    }

    private String getAnsiControlCode(int base) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(base + this.index)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }

    @Override
    public String toString() {
        if (this.highIntensity)
            return String.format("Bright %s", this.name);
        return this.name;
    }
}
