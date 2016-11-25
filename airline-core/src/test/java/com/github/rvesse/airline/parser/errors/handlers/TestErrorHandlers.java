package com.github.rvesse.airline.parser.errors.handlers;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.args.ArgsRequired;
import com.github.rvesse.airline.builder.ParserBuilder;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.ParseResult;
import com.github.rvesse.airline.parser.errors.ParseException;

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
}
