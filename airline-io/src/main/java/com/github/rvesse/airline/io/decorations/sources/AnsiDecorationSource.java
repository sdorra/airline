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
package com.github.rvesse.airline.io.decorations.sources;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.AnsiControlCodes;
import com.github.rvesse.airline.io.decorations.AnsiDecorationProvider;

public class AnsiDecorationSource<T extends AnsiDecorationProvider> implements ControlCodeSource<T> {

    @Override
    public String getControlCode(T attributeSource) {
        return attributeSource.getAnsiDecorationEnabledControlCode();
    }

    @Override
    public String getResetControlCode(T attributeSource) {
        return attributeSource.getAnsiDecorationDisabledControlCode();
    }

    @Override
    public String getFullResetControlCode() {
        return AnsiControlCodes.getGraphicsResetCode();
    }
}
