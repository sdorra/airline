package com.github.rvesse.airline.help.sections.common;

import com.github.rvesse.airline.help.sections.HelpFormat;

public class ProseSection extends BasicSection {
    
    public ProseSection(String title, String[] paragraphs) {
        this(title, 0, null, null, paragraphs);
    }
    
    public ProseSection(String title, int suggestedOrder, String[] paragraphs) {
        this(title, suggestedOrder, null, null, paragraphs);
    }

    public ProseSection(String title, int suggestedOrder, String preamble, String postamble, String[] paragaraphs) {
        super(title, suggestedOrder, preamble, postamble, HelpFormat.PROSE, paragaraphs);
    }

}
