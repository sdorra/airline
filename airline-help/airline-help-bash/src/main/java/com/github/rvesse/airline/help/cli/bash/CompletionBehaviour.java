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
package com.github.rvesse.airline.help.cli.bash;

/**
 * Possible completion behaviour for options/arguments 
 */
public enum CompletionBehaviour {
    /**
     * None, either this is a flag option (i.e. arity zero) or you want to limit
     * completions to those specified by the option meta-data
     */
    NONE,
    /**
     * Filenames, use standard filename completion if no other completions apply
     */
    FILENAMES,
    /**
     * Directories, use standard directory name completion if no other
     * completions apply
     */
    DIRECTORIES,
    /**
     * Use the completions from the option meta-data (if any) but treat them as
     * if they were filenames for additional completion
     */
    AS_FILENAMES,
    /**
     * Use the completions from the option meta-data (if any) but treat them as
     * if they were directory names for additional completion
     */
    AS_DIRECTORIES,
            
    /**
     * Commands from the CLI for which we are provided completions
     */
    CLI_COMMANDS,
    
    /**
     * OS System commands
     */
    SYSTEM_COMMANDS
}
