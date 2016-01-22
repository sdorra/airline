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
package com.github.rvesse.airline.help.sections;

/**
 * Enumeration of help formats, a {@link HelpSection} or {@link HelpHint} may
 * provide this but help generators are not obliged to follow this
 * 
 * @author rvesse
 *
 */
public enum HelpFormat {
    /**
     * Help format is unknown
     */
    UNKNOWN,
    /**
     * Help format is prose i.e. paragraphs of text
     */
    PROSE,
    /**
     * Help format is a list
     */
    LIST,
    /**
     * Help format is a table, each content block will represent a column of the
     * table
     */
    TABLE,
    /**
     * Help format is a table where headers are included as the first item of
     * each column
     */
    TABLE_WITH_HEADERS,
    /**
     * Help format is examples, the first content block is the examples and the
     * subsequent content block(s) are explanations for the examples
     */
    EXAMPLES,
    /**
     * Help represents some non-printable format, this can be used to create
     * special sections that carry extra data that is used by help generators in
     * some other way
     */
    NONE_PRINTABLE
}
