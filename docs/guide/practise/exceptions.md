---
layout: page
title: Exceptions and Error Handling
---

As with most Java libraries Airline uses Java exceptions as a way to signal problems.  Airline exceptions are unchecked exceptions (which means calling code is not required to handle them) however in order to provide a good user experience you will usually want to handle these.  The root type for Airline exception is simply {% include javadoc-ref.md class="ParseException" package="parser.errors" %} but there is a whole hierarchy of more specific exceptions below that:

- {% include javadoc-ref.md class="ParseException" package="parser.errors" %} - Base Airline Exception
    - {% include javadoc-ref.md class="ParseAliasCircularReferenceException" package="parser.errors" %} - Indicates a circular reference while resolving command aliases
    - {% include javadoc-ref.md class="ParseInvalidRestrictionException" package="parser.errors" %} - Indicates that a declared restriction annotation is not valid
    - {% include javadoc-ref.md class="ParseOptionConversionException" package="parser.errors" %} - Indicates that Airline could not successfully convert the raw string value into the Java type associated with the option/arguments
    - {% include javadoc-ref.md class="ParseOptionUnexpectedException" package="parser.errors" %} - Indicates that an option was used in an unexpected/invalid way e.g. trying to use an option in classic `getopt` style that does not have a short form
    - {% include javadoc-ref.md class="ParseRestrictionViolationException" package="parser.errors" %} - Indicates that a [Restriction](../restrictions/index.hml) was violated
        - {% include javadoc-ref.md class="ParseArgumentsIllegalValueException" package="parser.errors" %} - Indicates that an arguments value was not in the set of acceptable values
        - {% include javadoc-ref.md class="ParseArgumentsMissingException" package="parser.errors" %} - Indicates that values for an arguments were expected but not provided
        - {% include javadoc-ref.md class="ParseArgumentsUnexpectedException" package="parser.errors" %} - Indicates that unexpected arguments were received
        - {% include javadoc-ref.md class="ParseCommandMissingException" package="parser.errors" %} - Indicates that the user did not specify a command to execute and there was no suitable default command defined
        - {% include javadoc-ref.md class="ParseCommandUnrecognizedException" package="parser.errors" %} - Indicates that an invalid command/group name was specified
        - {% include javadoc-ref.md class="ParseOptionGroupException" package="parser.errors" %} - Indicates that a restriction that applied over a group of options e.g. [`@MutuallyExclusiveWith`](../annotations/mutually-exclusive-with.html) was violated
        - {% include javadoc-ref.md class="ParseOptionIllegalValueException" package="parser.errors" %} - Indicates that an options value was not in the set of acceptable values
        - {% include javadoc-ref.md class="ParseOptionMissingException" package="parser.errors" %} - Indicates that a required option was not specified
        - {% include javadoc-ref.md class="ParseOptionMissingValueException" package="parser.errors" %} - Indicates that an options value was expected but not provided
        - {% include javadoc-ref.md class="ParseOptionOutOfRangeException" package="parser.errors" %} - Indicates that an option/arguments value was not within the acceptable range of values
        - {% include javadoc-ref.md class="ParseTooManyArgumentsException" package="parser.errors" %} - Indicates that too many options/arguments were provided

### IO Exceptions

Some aspects of Airline that deal with IO, most notably the [Help subsystem](../help/index.html) will use standard Java `IOException` and these are checked exceptions which you should handle appropriately.

## Error Control Flow

As with most things in Airline you have some ability to customise the error control flow.  While some things are considered as fatal errors, e.g. missing/invalid metadata annotations, and always throw errors in the actual parsing phase you can control how errors are handled.  Error handling is done by the `ParserErrorHandler` interface, the desired handler can be defined in your parser configuration via the [`@Parser`](../annotations/parser.html) annotations `errorHandler` field e.g.

```java
@Parser(errorHandler = CollectAll.class)
```
The default error handler is {% include javadoc-ref.md class="FailFast" package="parser.errors.handlers" %}, this error handler will simply throw the first parsing error encountered i.e. parsing halts immediately on encountering an error.

Sometimes you may wish to collect up all the errors before failing in which case the {% include javadoc-ref.md class="FailAll" package="parser.errors.handlers" %} handler will collect and throw either a single exception if only 1 error was encountered or an aggregated exception containing all the errors as suppressed exceptions.

Alternatively you may wish to collect up all the errors and then make an intelligent decision on how to proceed.  For example if the user has specified your help option then you probably want to show them your help message regardless of the errors.  In this case the {% include javadoc-ref.md class="CollectAll" package="parser.errors.handlers" %} handler will allow you to do this.

In order to act intelligently both the `Cli` and the `SingleCommand` classes provide a `parseWithResult(String... args)` method that returns a `ParseResult<T>` instance.  This can be used to inspect the results of parsing and act appropriately.  For example if we wanted to show the error messages and the help output we might do the following:

```java
public static void main(String[] args) {
        com.github.rvesse.airline.Cli<ExampleRunnable> cli = new com.github.rvesse.airline.Cli<ExampleRunnable>(ShipItCli.class);
        try {
            // Parse with a result to allow us to inspect the results of parsing
            ParseResult<ExampleRunnable> result = cli.parseWithResult(args);
            if (result.wasSuccessful()) {
                // Parsed successfully, so just run the command and exit
                System.exit(result.getCommand().run());
            } else {
                // Parsing failed
                // Display errors and then the help information
                System.err.println(String.format("%d errors encountered:", result.getErrors().size()));
                int i = 1;
                for (ParseException e : result.getErrors()) {
                    System.err.println(String.format("Error %d: %s", i, e.getMessage()));
                    i++;
                }
                
                System.err.println();
                
                com.github.rvesse.airline.help.Help.<ExampleRunnable>help(cli.getMetadata(), Arrays.asList(args), System.err);
            }
        } catch (Exception e) {
            // Errors should be being collected so if anything is thrown it is unexpected
            System.err.println(String.format("Unexpected error: %s", e.getMessage()));
            e.printStackTrace(System.err);
        }
        
        // If we got here we are exiting abnormally
        System.exit(1);
    }
```