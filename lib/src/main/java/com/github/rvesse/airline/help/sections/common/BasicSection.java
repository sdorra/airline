package com.github.rvesse.airline.help.sections.common;

import com.github.rvesse.airline.help.sections.HelpFormat;
import com.github.rvesse.airline.help.sections.HelpSection;

public class BasicSection extends BasicHint implements HelpSection {
    
    private final String title, postamble;
    private final int order;

    public BasicSection(String title, int suggestedOrder, String preamble, String postamble, HelpFormat format, String[]... blocks) {
        super(preamble, format, blocks);
        this.title = title;
        this.postamble = postamble;
        this.order = suggestedOrder;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getPostamble() {
        return this.postamble;
    }

    @Override
    public int suggestedOrder() {
        return this.order;
    }

}
