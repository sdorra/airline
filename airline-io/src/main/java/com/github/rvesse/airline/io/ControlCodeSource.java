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
 * Interface for classes that provide control codes
 *
 * @param <T>
 *            Attribute source
 */
public interface ControlCodeSource<T> {

    /**
     * Translates the attribute source into a control code that can be passed to
     * an input/output stream
     * 
     * @param attributeSource
     *            Attribute source
     * @return Control code
     */
    public String getControlCode(T attributeSource);

    /**
     * Gets a reset code that can be used to reset any changes previously made
     * by the given attribute
     * 
     * @param attributeSource
     *            Attribute source
     * 
     * @return Control code
     */
    public String getResetControlCode(T attributeSource);

    /**
     * Gets a reset code that can be used to reset any changes previously made
     * by any attributes of the type supported by this source
     * 
     * @return Control Code
     */
    public String getFullResetControlCode();
}
