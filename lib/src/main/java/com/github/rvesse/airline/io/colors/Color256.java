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
 * 256 colour i.e. palette of 256 colors that most modern terminals will support
 *
 */
public class Color256 implements AnsiColorProvider {

    private final int color;

    public Color256(int color) {
        this.color = checkColor(color);
    }

    private int checkColor(int c) {
        if (c < 0 || c > 255)
            throw new IllegalArgumentException(String.format(
                    "Color was given value %d but only values in the range 0-255 are acceptable", c));
        return c;
    }

    @Override
    public String getAnsiForegroundControlCode() {
        return getAnsiColorCode(AnsiControlCodes.FOREGROUND_EXTENDED);
    }

    @Override
    public String getAnsiBackgroundControlCode() {
        return getAnsiColorCode(AnsiControlCodes.BACKGROUND_EXTENDED);
    }

    @Override
    public boolean usesExtendedColors() {
        return true;
    }

    private String getAnsiColorCode(int mode) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(mode)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(AnsiControlCodes.COLOR_MODE_256)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(this.color)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("%d", this.color);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Color256))
            return false;

        Color256 c = (Color256) other;
        return this.color == c.color;
    }
}
