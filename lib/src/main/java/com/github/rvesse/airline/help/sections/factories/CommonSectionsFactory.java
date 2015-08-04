package com.github.rvesse.airline.help.sections.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.annotations.help.Discussion;
import com.github.rvesse.airline.annotations.help.Examples;
import com.github.rvesse.airline.annotations.help.ExitCodes;
import com.github.rvesse.airline.help.sections.HelpSection;
import com.github.rvesse.airline.help.sections.common.DiscussionSection;
import com.github.rvesse.airline.help.sections.common.ExamplesSection;
import com.github.rvesse.airline.help.sections.common.ExitCodesSection;

/**
 * A help section factory that implements the common sections built into Airline
 */
public class CommonSectionsFactory implements HelpSectionFactory {

    @Override
    public HelpSection createSection(Annotation annotation) {
        if (annotation instanceof Examples) {
            Examples ex = (Examples) annotation;
            return new ExamplesSection(ex.examples(), ex.descriptions());
        } else if (annotation instanceof Discussion) {
            return new DiscussionSection(((Discussion) annotation).paragraphs());
        } else if (annotation instanceof ExitCodes) {
            ExitCodes exits = (ExitCodes) annotation;
            return new ExitCodesSection(exits.codes(), exits.descriptions());
        }
        return null;
    }

}
