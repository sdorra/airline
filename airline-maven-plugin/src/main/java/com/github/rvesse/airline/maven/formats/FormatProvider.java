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
package com.github.rvesse.airline.maven.formats;

import com.github.rvesse.airline.help.CommandGroupUsageGenerator;
import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.GlobalUsageGenerator;

/**
 * Interface that provides a help format
 * 
 * @author rvesse
 *
 */
public interface FormatProvider {

    /**
     * Gets the default name for this format used when automatically registering
     * {@code ServiceLoader} discovered implementations
     * 
     * @return
     */
    public String getDefaultMappingName();

    /**
     * Gets the extension to apply to generated help files, this
     * <strong>should</strong> include the {@code .} character
     * 
     * @param options
     *            Format options
     * @return File extension
     */
    public String getExtension(FormatOptions options);

    /**
     * Gets a command usage generator for the format that uses the given options
     * 
     * @param options
     *            Options
     * @return A command usage generator or {@code null} if this format does not
     *         support command help
     */
    public CommandUsageGenerator getCommandGenerator(FormatOptions options);

    /**
     * Gets a command group usage generator for the format that uses the given
     * options
     * 
     * @param options
     *            Options
     * @return A command group usage generator or {@code null} if this format
     *         does not support command group help
     */
    public CommandGroupUsageGenerator<Object> getGroupGenerator(FormatOptions options);

    /**
     * Gets a global usage generator for the format that uses the given options
     * 
     * @param options
     *            Options
     * @return A global usage generator or {@code null} if this format does not
     *         support global help
     */
    public GlobalUsageGenerator<Object> getGlobalGenerator(FormatOptions options);
}
