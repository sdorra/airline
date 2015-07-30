package com.github.rvesse.airline.help.sections;

/**
 * Enumeration of help formats, a {@link HelpSection} or {@link HelpHint} may
 * provide this but help generators are not obliged to follow this
 * 
 * @author rvesse
 *
 */
public enum HelpFormat {
    /**
     * Help format is unknown
     */
    UNKNOWN,
    /**
     * Help format is prose i.e. paragraphs of text
     */
    PROSE,
    /**
     * Help format is a list
     */
    LIST,
    /**
     * Help format is a table, each content block will represent a column of the table
     */
    TABLE,
    /**
     * Help format is a table where headers are included as the first item of each column
     */
    TABLE_WITH_HEADERS,
    /**
     * Help format is examples, the first content block is the examples and the subsequent content block(s) are explanations for the examples
     */
    EXAMPLES
}
