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

/**
 * Interface that may be implemented by colour providers
 *
 */
public interface AnsiColorProvider {

    /**
     * Gets the ANSI control code for setting the background colour
     * 
     * @return Background control code
     */
    public abstract String getAnsiBackgroundControlCode();

    /**
     * Gets the ANSI control code for setting the foreground colour
     * 
     * @return Foreground control code
     */
    public abstract String getAnsiForegroundControlCode();

    /**
     * Gets whether extended colours are used as this will affect the ANSI reset
     * sequence that needs to be used
     * 
     * @return True if extended colours are used, false otherwise
     */
    public boolean usesExtendedColors();

}
