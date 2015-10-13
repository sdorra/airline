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
package com.github.rvesse.airline.help.sections.factories;

import java.lang.annotation.Annotation;

import com.github.rvesse.airline.annotations.help.Copyright;
import com.github.rvesse.airline.annotations.help.Discussion;
import com.github.rvesse.airline.annotations.help.Examples;
import com.github.rvesse.airline.annotations.help.HideSection;
import com.github.rvesse.airline.annotations.help.ProseSection;
import com.github.rvesse.airline.annotations.help.ExitCodes;
import com.github.rvesse.airline.help.sections.HelpFormat;
import com.github.rvesse.airline.help.sections.HelpSection;
import com.github.rvesse.airline.help.sections.common.BasicSection;
import com.github.rvesse.airline.help.sections.common.CommonSections;
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
        } else if (annotation instanceof HideSection) {
            HideSection hide = (HideSection) annotation;
            return new BasicSection(hide.title(), 0, null, null, HelpFormat.NONE_PRINTABLE, new String[0]);
        } else if (annotation instanceof ProseSection) {
            ProseSection prose = (ProseSection) annotation;
            return new BasicSection(prose.title(), prose.suggestedOrder(), null, null, HelpFormat.PROSE,
                    prose.paragraphs());
        } else if (annotation instanceof Copyright) {
            Copyright copyright = (Copyright) annotation;
            String line = String.format("Copyright (c) %s %s%s", copyright.holder(), copyright.startYear(),
                    copyright.endYear() > copyright.startYear() ? String.format("-%s", copyright.endYear()) : "");
            return new BasicSection(CommonSections.TITLE_COPYRIGHT, CommonSections.ORDER_COPYRIGHT, null, null,
                    HelpFormat.PROSE, new String[] { line });
        }
        return null;
    }

}
