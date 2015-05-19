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
