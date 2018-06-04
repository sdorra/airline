---
layout: page
title: Custom Option Parsers
---

Option parsing is provided to Airline by the instances of the `OptionParser` interface.  Airline ships with a variety of these which implement common option parsing styles which are detailed in the [Parser Overview](index.html).

It is also possible to provide your own custom implementations if you wish to more closely control the option parsing process yourself, here we work through an example implementation.

### The `OptionParser` interface

A custom option parser must implement the `OptionParser` interface which is pretty simple:

```java
public interface OptionParser<T> {

    /**
     * Parses one/more options from the token stream
     * 
     * @param tokens
     *            Tokens
     * @param state
     *            Current parser state
     * @param allowedOptions
     *            Allowed options at this point of the parsing
     * @return New parser state, may return {@code null} if this parser could
     *         not parse the next token as an option
     */
    public abstract ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state,
            List<OptionMetadata> allowedOptions);
}
```
Let's start by reviewing what the interface gives us. Firstly we get `tokens` which is a peeking iterator over the inputs being parsed, this allows us to `peek()` the next token as well as consume it with `next()` once we determine whether we can actually parse an option.

Secondly we get the parser `state` which provides access to useful information such as the parser configuration and which we will need to manipulate to indicate options we have successfully parsed.

Finally we get `allowedOptions` which is the list of options that are currently in-scope. Since our parser may be called at multiple stages in the parsing process different options may be in-scope each time we are called.

In terms of return value there are three things we can return:

1. A new `ParseState` indicating what we have parsed
2. A new `ParseState` indicating we encountered an exception
2. A `null` to indicate that we could not parse anything and other option parsers should be tried

Where possible parsers should prefer option 3 to 2 i.e. If they cannot parse anything they should not modify `tokens` or generate a new state and just return `null` so other configured parser implementations will be tried. However for some more complex parsers you may already have consumed some input by the time you realise that you cannot successfully parse an option in which case communicating an exception is the correct thing to do.

### Simple Custom Option Parser

If you simply wish to change the name and value separator then you can trivially do this by sub-classing {% include javadoc-ref.md class="AbstractNameValueOptionParser" package="parser.options" %} and specify the desired separator e.g.

```java
package com.github.rvesse.airline.examples.userguide.parser.options;

import com.github.rvesse.airline.parser.options.AbstractNameValueOptionParser;

/**
 * An option parser where the name and value are colon separated i.e.
 * {@code -name:value}
 *
 * @param <T>
 *            Command type
 */
public class ColonSeparatedOptionParser<T> extends AbstractNameValueOptionParser<T> {

    /**
     * Creates a new parser instance
     */
    public ColonSeparatedOptionParser() {
        super(':');
    }

}
```
This provides us a parser that uses `:` as the separator so `--name:example` would set the `--name` option to the value `example`

### Complex Custom Option Parsers

*TODO*