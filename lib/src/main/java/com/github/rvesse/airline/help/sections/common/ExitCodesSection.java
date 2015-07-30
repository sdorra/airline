package com.github.rvesse.airline.help.sections.common;

import com.github.rvesse.airline.help.sections.HelpFormat;

public class ExitCodesSection extends BasicSection {

    public ExitCodesSection(int[] exitCodes, String[] exitCodeDescriptions) {
        super(CommonSections.TITLE_EXIT_CODES, CommonSections.ORDER_EXIT_CODES,
                "This command returns one of the following exit codes:", null, HelpFormat.TABLE, toStrings(exitCodes),
                exitCodeDescriptions);
    }

    private static String[] toStrings(int[] is) {
        String[] data = new String[is.length];
        for (int i = 0; i < is.length; i++) {
            data[i] = Integer.toString(is[i]);
        }
        return data;
    }

}
