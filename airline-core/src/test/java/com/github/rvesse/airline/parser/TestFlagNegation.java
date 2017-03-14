package com.github.rvesse.airline.parser;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.args.ArgsFlagNegation;
import com.github.rvesse.airline.builder.ParserBuilder;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.errors.ParseArgumentsUnexpectedException;

public class TestFlagNegation {

    @Test
    public void flagNegationConfigure() {
        ParserMetadata<ArgsFlagNegation> parserConfig = new ParserBuilder<ArgsFlagNegation>()
                .withFlagNegationPrefix("--no-").build();
        SingleCommand<ArgsFlagNegation> parser = SingleCommand.singleCommand(ArgsFlagNegation.class, parserConfig);

        ArgsFlagNegation args = parser.parse();
        Assert.assertFalse(args.falseFlag);
        Assert.assertTrue(args.trueFlag);

        // If negation prefix is configured using the correctly prefix version
        // of the name causes the flag value to be set to false instead of true

        args = parser.parse("--false");
        Assert.assertTrue(args.falseFlag);

        args = parser.parse("--no-false");
        Assert.assertFalse(args.falseFlag);

        args = parser.parse("--true");
        Assert.assertTrue(args.trueFlag);

        args = parser.parse("--no-true");
        Assert.assertFalse(args.trueFlag);
    }

    @Test
    public void flagNegationNotConfigured() {
        SingleCommand<ArgsFlagNegation> parser = SingleCommand.singleCommand(ArgsFlagNegation.class);

        ArgsFlagNegation args = parser.parse();
        Assert.assertFalse(args.falseFlag);
        Assert.assertTrue(args.trueFlag);

        // If negation is not configured all names cause flags to be set to true

        args = parser.parse("--false");
        Assert.assertTrue(args.falseFlag);

        args = parser.parse("--no-false");
        Assert.assertTrue(args.falseFlag);

        args = parser.parse("--true");
        Assert.assertTrue(args.trueFlag);

        args = parser.parse("--no-true");
        Assert.assertTrue(args.trueFlag);
    }

    @Test(expectedExceptions = ParseArgumentsUnexpectedException.class)
    public void flagNegationUnregistedPrefixUsed() {
        ParserMetadata<ArgsFlagNegation> parserConfig = new ParserBuilder<ArgsFlagNegation>()
                .withFlagNegationPrefix("--invert-").build();
        SingleCommand<ArgsFlagNegation> parser = SingleCommand.singleCommand(ArgsFlagNegation.class, parserConfig);

        parser.parse("--invert-true");
    }
}
