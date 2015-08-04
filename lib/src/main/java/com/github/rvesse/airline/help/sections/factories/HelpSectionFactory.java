package com.github.rvesse.airline.help.sections.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.help.sections.HelpSection;

public interface HelpSectionFactory {

    public abstract HelpSection createSection(Annotation annotation);
}
