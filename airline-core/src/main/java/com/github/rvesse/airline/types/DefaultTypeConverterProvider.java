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
package com.github.rvesse.airline.types;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.ParseState;

/**
 * Default type converter provider which simply inspects the {@link ParseState}
 * given and returns the the type converter specified on the
 * {@link ParserMetadata} provided by the parse state
 * 
 * @author rvesse
 *
 */
public class DefaultTypeConverterProvider implements TypeConverterProvider {

    @Override
    public <T> TypeConverter getTypeConverter(OptionMetadata option, ParseState<T> state) {
        return state.getParserConfiguration().getTypeConverter();
    }

    @Override
    public <T> TypeConverter getTypeConverter(ArgumentsMetadata arguments, ParseState<T> state) {
        return state.getParserConfiguration().getTypeConverter();
    }

}
