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

import com.github.rvesse.airline.help.sections.HelpFormat;

public class ExamplesSection extends BasicSection {

    public ExamplesSection(String[] examples, String[] exampleDescriptions) {
        super(CommonSections.TITLE_EXAMPLES, CommonSections.ORDER_EXAMPLES, null, null, HelpFormat.EXAMPLES, examples,
                exampleDescriptions);
    }

}
