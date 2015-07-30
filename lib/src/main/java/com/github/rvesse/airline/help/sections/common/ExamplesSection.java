package com.github.rvesse.airline.help.sections.common;

import com.github.rvesse.airline.help.sections.HelpFormat;

public class ExamplesSection extends BasicSection {

    public ExamplesSection(String[] examples, String[] exampleDescriptions) {
        super(CommonSections.TITLE_EXAMPLES, CommonSections.ORDER_EXAMPLES, null, null, HelpFormat.EXAMPLES, examples,
                exampleDescriptions);
    }

}
