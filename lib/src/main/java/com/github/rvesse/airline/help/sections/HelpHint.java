package com.github.rvesse.airline.help.sections;

/**
 * Interface for classes that provide additional help information
 */
public interface HelpHint {

    /**
     * Gets the preamble text that should be included
     * 
     * @return Preamble text
     */
    public String getPreamble();

    /**
     * Gets the format of the provided help information
     * 
     * @return Help format
     */
    public HelpFormat getFormat();

    /**
     * Gets the number of content blocks provided
     * <p>
     * Help generators should consult the {@link #getFormat()} return value to
     * determine how to format the content blocks but they are not required to
     * do so
     * </p>
     * 
     * @return Number of content blocks
     */
    public int numContentBlocks();

    /**
     * Gets the content block with the given number
     * 
     * @param blockNumber
     *            Block number
     * @return Content Block
     */
    public String[] getContentBlock(int blockNumber);
}
