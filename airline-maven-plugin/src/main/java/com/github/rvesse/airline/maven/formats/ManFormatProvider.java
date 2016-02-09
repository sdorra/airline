/**
 * Copyright (C) 2010-16 the original author or authors.
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
package com.github.rvesse.airline.maven.formats;

import com.github.rvesse.airline.help.CommandGroupUsageGenerator;
import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.GlobalUsageGenerator;
import com.github.rvesse.airline.help.man.ManCommandUsageGenerator;
import com.github.rvesse.airline.help.man.ManGlobalUsageGenerator;
import com.github.rvesse.airline.help.man.ManMultiPageGlobalUsageGenerator;

public class ManFormatProvider implements FormatProvider {

    @Override
    public String getExtension(FormatOptions options) {
        return String.format(".%d", options.getManSection());
    }

    @Override
    public CommandUsageGenerator getCommandGenerator(FormatOptions options) {
        return new ManCommandUsageGenerator(options.getManSection(), options.includeHidden());
    }

    @Override
    public CommandGroupUsageGenerator<Object> getGroupGenerator(FormatOptions options) {
        return null;
    }

    @Override
    public GlobalUsageGenerator<Object> getGlobalGenerator(FormatOptions options) {
        if (options.useMultipleFiles()) {
            return new ManMultiPageGlobalUsageGenerator<Object>(options.getManSection(), options.includeHidden());
        } else {
            return new ManGlobalUsageGenerator<Object>(options.getManSection(), options.includeHidden());
        }
    }

    @Override
    public String getDefaultMappingName() {
        return "MAN";
    }

}
