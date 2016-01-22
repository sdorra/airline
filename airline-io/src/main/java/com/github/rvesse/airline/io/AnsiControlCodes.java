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
package com.github.rvesse.airline.io;

/**
 * Constants
 * 
 * @author rvesse
 *
 */
public class AnsiControlCodes {

    /**
     * Private constructor to prevent instantiation
     */
    AnsiControlCodes() {
    }

    /**
     * Standard ANSI escape sequence
     */
    public static final String ESCAPE = "\u001B[";

    /**
     * Character used to separate parameters in ANSI escape sequences
     */
    public static final char PARAM_SEPARATOR = ';';
    
    /**
     * Command code for setting the graphics rendition
     */
    public static final char SELECT_GRAPHIC_RENDITION = 'm';
    
    /**
     * Character used to request reset 
     */
    public static final char RESET = '0';
    
    public static final int BOLD = 1;
    
    public static final int FAINT = 2;
    
    public static final int ITALIC = 3;
    
    public static final int UNDERLINE = 4;
    
    public static final int BLINK_SLOW = 5;
    
    public static final int BLINK_RAPID = 6;
    
    public static final int IMAGE_NEGATIVE = 7;
    
    public static final int CONCEAL = 8;
    
    public static final int STRIKE_THROUGH = 9;
    
    public static final int BOLD_OFF = 21;
        
    public static final int NORMAL_INTENSITY = 22;
    
    public static final int ITALIC_OFF = 23;
    
    public static final int UNDERLINE_OFF = 24;
    
    public static final int BLINK_OFF = 25;
    
    public static final int IMAGE_POSITIVE = 27;
    
    public static final int REVEAL = 28;
    
    public static final int STRIKE_THROUGH_OFF = 29;

    /**
     * Control Code for setting the foreground colour
     */
    public static final int FOREGROUND = 30;
    
    /**
     * Control Code for setting the foreground colour to an extended colour
     */
    public static final int FOREGROUND_EXTENDED = 38;

    /**
     * Control Code for setting the background colour
     */
    public static final int BACKGROUND = 40;
    
    /**
     * Control Code for setting the background colour to an extended colour
     */
    public static final int BACKGROUND_EXTENDED = 48;
    
    /**
     * Control code for resetting the foreground colour to the default
     */
    public static final int DEFAULT_FOREGROUND = 39;

    /**
     * Control code for resetting the background colour to the default
     */
    public static final int DEFAULT_BACKGROUND = 49;
    
    /**
     * Control Code for setting the high intensity variant of the foreground colour
     */
    public static final int FOREGROUND_BRIGHT = 90;
    
    /**
     * Control Code for setting the high intensity variant of the background colour
     */
    public static final int BACKGROUND_BRIGHT = 100;
    
    /**
     * 24 bit extended colour mode
     */
    public static final char COLOR_MODE_TRUE = '2';

    /**
     * 256 colour extended colour mode
     */
    public static final char COLOR_MODE_256 = '5';

    /**
     * Provides the ANSI full graphics reset code
     * 
     * @return ANSI Full graphics reset code
     */
    public static String getGraphicsResetCode() {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(ESCAPE)
               .append(RESET)
               .append(SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }
}
