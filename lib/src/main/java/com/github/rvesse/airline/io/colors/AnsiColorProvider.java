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

}
