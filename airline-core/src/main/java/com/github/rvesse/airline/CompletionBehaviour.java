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
package com.github.rvesse.airline;

/**
 * Possible completion behaviour for options/arguments 
 */
public class CompletionBehaviour {
    /**
     * None, either this is a flag option (i.e. arity zero) or you want to limit
     * completions to those specified by the option metadata
     */
    public static final int NONE = 0;
    /**
     * Filenames, use standard filename completion if no other completions apply
     */
    public static final int FILENAMES = 1;
    /**
     * Directories, use standard directory name completion if no other
     * completions apply
     */
    public static final int DIRECTORIES = 2;
    /**
     * Use the completions from the option metadata (if any) but treat them as
     * if they were filenames for additional completion
     */
    public static final int AS_FILENAMES = 3;
    /**
     * Use the completions from the option metadata (if any) but treat them as
     * if they were directory names for additional completion
     */
    public static final int AS_DIRECTORIES = 4;
            
    public static final int CLI_COMMANDS = 5;
    
    public static final int SYSTEM_COMMANDS = 6;;
}
