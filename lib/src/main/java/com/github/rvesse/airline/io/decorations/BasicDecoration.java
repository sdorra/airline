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
package com.github.rvesse.airline.io.decorations;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * Support for the basic ANSI decorations
 * @author rvesse
 *
 */
public enum BasicDecoration implements AnsiDecorationProvider {

    BOLD(AnsiControlCodes.BOLD, AnsiControlCodes.NORMAL_INTENSITY, "Bold"),
    FAINT(AnsiControlCodes.FAINT, AnsiControlCodes.NORMAL_INTENSITY, "Faint"),
    ITALIC(AnsiControlCodes.ITALIC, AnsiControlCodes.ITALIC_OFF, "Italic"),
    UNDERLINE(AnsiControlCodes.UNDERLINE, AnsiControlCodes.UNDERLINE_OFF, "Underline"),
    BLINK_SLOW(AnsiControlCodes.BLINK_SLOW, AnsiControlCodes.BLINK_OFF, "Blink (Slow)"),
    BLINK_RAPID(AnsiControlCodes.BLINK_RAPID, AnsiControlCodes.BLINK_OFF, "Blink (Rapid)"),
    IMAGE_NEGATIVE(AnsiControlCodes.IMAGE_NEGATIVE, AnsiControlCodes.IMAGE_POSITIVE, "Image Negative"),
    CONCEAL(AnsiControlCodes.CONCEAL, AnsiControlCodes.REVEAL, "Concealed"),
    STRIKE_THROUGH(AnsiControlCodes.STRIKE_THROUGH, AnsiControlCodes.STRIKE_THROUGH_OFF, "Strike-Through");
    
    private final int enableCode, disableCode;
    private final String name;
    
    private BasicDecoration(int enable, int disable, String name) {
        this.enableCode = enable;
        this.disableCode = disable;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getAnsiDecorationEnabledControlCode() {
        return getAnsiControlCode(this.enableCode);
    }

    @Override
    public String getAnsiDecorationDisabledControlCode() {
        return getAnsiControlCode(this.disableCode);
    }
    
    private String getAnsiControlCode(int code) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(code)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }
    
}
