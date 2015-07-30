/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
