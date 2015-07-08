package com.github.rvesse.airline.parser;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.parser.aliases.AliasArgumentsParser;
import com.github.rvesse.airline.parser.errors.ParseException;

public class TestAliasArgumentsParser {

    private static List<String> parse(String value) {
        return AliasArgumentsParser.parse(value);
    }
    
    @Test
    public void alias_arguments_01() {
        List<String> args = parse("foo bar");
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "foo");
        Assert.assertEquals(args.get(1), "bar");
    }
    
    @Test
    public void alias_arguments_02() {
        List<String> args = parse("\"foo\" bar");
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "foo");
        Assert.assertEquals(args.get(1), "bar");
    }
    
    @Test
    public void alias_arguments_03() {
        List<String> args = parse("foo \"bar\"");
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "foo");
        Assert.assertEquals(args.get(1), "bar");
    }
    
    @Test
    public void alias_arguments_04() {
        List<String> args = parse("\"foo\" \"bar\"");
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "foo");
        Assert.assertEquals(args.get(1), "bar");
    }
    
    @Test
    public void alias_arguments_whitespace_escapes_01() {
        // Whitespace escaping in unquoted arguments
        List<String> args = parse("foo\\ bar");
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), "foo bar");
    }
    
    @Test
    public void alias_arguments_whitespace_escapes_02() {
        // Whitespace escaping in unquoted arguments
        List<String> args = parse("foo\\ \\ \\ bar");
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), "foo   bar");
    }
    
    @Test
    public void alias_arguments_whitespace_escapes_03() {
        // Whitespace escaping in unquoted arguments
        List<String> args = parse("foo bar\\");
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "foo");
        Assert.assertEquals(args.get(1), "bar\\");
    }
    
    @Test
    public void alias_arguments_whitespace_escapes_04() {
        // Whitespace escaping in unquoted arguments
        List<String> args = parse("foo bar\\ ");
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "foo");
        Assert.assertEquals(args.get(1), "bar ");
    }
    
    @Test
    public void alias_arguments_quote_escapes_01() {
        // Quote escaping in quoted arguments
        List<String> args = parse("\"foo\\\" bar\"");
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), "foo\" bar");
    }
    
    @Test
    public void alias_arguments_quote_escapes_02() {
        // Quote escaping in quoted arguments
        List<String> args = parse("\"foo\\\"\\\"bar\"");
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), "foo\"\"bar");
    }
    
    @Test(expectedExceptions = ParseException.class)
    public void alias_arguments_bad_01() {
        // Mis-matched quotes
        parse("\"foo bar");
    }
}
