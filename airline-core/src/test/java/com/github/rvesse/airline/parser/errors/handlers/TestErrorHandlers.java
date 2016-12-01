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
package com.github.rvesse.airline.parser.errors.handlers;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.args.ArgsRequired;
import com.github.rvesse.airline.builder.ParserBuilder;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.ParseResult;
import com.github.rvesse.airline.parser.errors.ParseException;
import com.github.rvesse.airline.restrictions.Strings;

public class TestErrorHandlers {

    private <T> ParserMetadata<T> prepareParser(ParserErrorHandler handler) {
        return new ParserBuilder<T>().withErrorHandler(handler).build();
    }

    @Test(expectedExceptions = ParseException.class)
    public void errorHandlerFailFast() {
        SingleCommand
                .<ArgsRequired> singleCommand(ArgsRequired.class, this.<ArgsRequired> prepareParser(new FailFast()))
                .parse();
    }

    @Test
    public void errorHandlerCollectAll() {
        ParseResult<ArgsRequired> result = SingleCommand
                .<ArgsRequired> singleCommand(ArgsRequired.class, this.<ArgsRequired> prepareParser(new CollectAll()))
                .parseWithResult();
        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(result.getErrors().size(), 1);
    }

    @Test
    public void errorHandlerFailAll() {
        try {
            ParseResult<Strings> result = SingleCommand
                    .<Strings> singleCommand(Strings.class, this.<Strings> prepareParser(new FailAll()))
                    .parseWithResult("--not-empty", "", "--not-blank", "  ");
        } catch (ParseException e) {
            Assert.assertEquals(e.getSuppressed().length, 2);
        }
    }
}
