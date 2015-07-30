package com.github.rvesse.airline.help.sections;

/**
 * Interface for classes that represents additional help sections
 */
public interface HelpSection extends HelpHint {

    /**
     * Gets the title for the section
     * 
     * @return Section title
     */
    public String getTitle();

    /**
     * Gets the post-amble text that should be included at the end of the
     * section
     * 
     * @return
     */
    public String getPostamble();

    /**
     * Gets an integer indicating the suggested order that a help generator
     * should include this section in
     * <p>
     * A value of zero indicates that the help generator can decide where to
     * place this section, a positive value indicates it should be placed after
     * the default sections while a negative value indicates it should be placed
     * before the default sections. There is however no guarantee that a help
     * generator will honour the order given.
     * </p>
     * 
     * @return Suggested order
     */
    public int suggestedOrder();

}
