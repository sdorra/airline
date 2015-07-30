package com.github.rvesse.airline.help.sections.common;

import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.help.sections.HelpFormat;
import com.github.rvesse.airline.help.sections.HelpHint;

public class BasicHint implements HelpHint {
    
    private final String preamble;
    private final HelpFormat format;
    private List<String[]> blocks = new ArrayList<String[]>();
    
    public BasicHint(String preamble, HelpFormat format, String[]... blocks) {
        this.preamble = preamble;
        this.format = format != null ? format : HelpFormat.UNKNOWN;
        for (String[] block : blocks) {
            this.blocks.add(block);
        }
    }

    @Override
    public String getPreamble() {
        return this.preamble;
    }

    @Override
    public HelpFormat getFormat() {
        return this.format;
    }

    @Override
    public int numContentBlocks() {
        return this.blocks.size();
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        return this.blocks.get(blockNumber);
    }

}
