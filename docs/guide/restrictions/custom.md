---
layout: page
title: Custom Restrictions
---

{% include toc.html %}

# Creating a Custom Restriction

As already seen Airline contains a large range of pre-built [Restrictions](index.html) that allow you to easily enforce common value restrictions through annotations avoiding the boiler-plate manual code required by many Command Line libraries.  Just like other subsystems restrictions are fully extensible with custom restrictions, custom restrictions consist of the following parts:

1. Your custom annotation for applying the restriction
2. A factory that understands how to translate your annotation into an appropriate restriction implementation
3. Your restriction implementation
4. A `ServiceLoader` manifest that registers your restriction factories

On this page we'll work through all these steps and show you how to create a custom restriction.

## Custom Annotation

Since Airline is annotation driven any custom restriction starts by defining the annotation you wish to use to apply it to fields.  Here is an example annotation definition:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface MultipleOf {

    /**
     * The value that we must be a multiple of
     * 
     * @return Value
     */
    int value();
}
```
The most important aspect is the `@Retention` annotation to specify that this annotation is retained and available at runtime.  We also use the `@Target` annotation to constrain where users may apply our annotation.

Finally we define any fields that we wish to use to define our restriction, in this case we have a single field with the special name `value` that allows us to specify it as `@MultipleOf(2)` without explicitly having to state the field name.

More complex restrictions will obviously need more complex corresponding annotations e.g. [`@Pattern`](../annotations/pattern.html).  They will want to avoid usage of the special `value` field and may wish to use `default` values for some fields so default behaviours for a restriction can be provided without having the user specify every field.

## Restriction Factory

There are three restriction factory interfaces and depending on the kind of restriction being created we need to implement at least one of these:

- `OptionRestrictionFactory` - Restrictions on `@Option` annotated fields
- `ArgumentsRestrictionFactory` - Restrictions on `@Arguments` annotated fields
- `GlobalRestrictionFactory` - Restrictions on `@Command` and `@Cli`

Typically if you implement either of the first two you probably want to implement both, if you implement the third you only want to implement that.

So let's see a restriction factory for our annotation:

```java
public class MultipleRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        if (annotation instanceof MultipleOf) {
            return create((MultipleOf) annotation);
        }
        return null;
    }

    @Override
    public List<Class<? extends Annotation>> supportedArgumentsAnnotations() {
        return Collections.<Class<? extends Annotation>> singletonList(MultipleOf.class);
    }

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        if (annotation instanceof MultipleOf) {
            return create((MultipleOf) annotation);
        }
        return null;
    }

    @Override
    public List<Class<? extends Annotation>> supportedOptionAnnotations() {
        return Collections.<Class<? extends Annotation>> singletonList(MultipleOf.class);
    }
    
    private MultipleOfRestriction create(MultipleOf multipleOf) {
        return new MultipleOfRestriction(multipleOf.value());
    }

}
```

Here we can see that our factory declares that it supports our `@MultipleOf` restriction and creates an instance of our actual restriction `MultipleOfRestriction`.  The above is a fairly simplistic implementation, for more complex examples see {% include github-ref.md class="RangeRestrictionFactory" package="restrictions.factories" %} or {% include github-ref.md class="RequireFromRestrictionFactory" package="restrictions.factories" %}.

## Restriction Implementation

The restriction implementation is the piece of code that actually checks and enforces the restriction.  Depending on the type of restriction being implemented you will implement one/more of the following interfaces:

- {% include javadoc-ref.md class="OptionRestriction" package="restrictions" %} - Restrictions on `@Option` annotated fields
- {% include javadoc-ref.md class="ArgumentsRestriction" package="restrictions" %} - Restrictions on `@Arguments` annotated fields
- {% include javadoc-ref.md class="GlobalRestriction" package="restrictions" %} - Restrictions on `@Cli` or `@Command`

Typically if you implement either of the first two you probably want to implement both.  For this common scenario you can extend {% include javadoc-ref.md class="AbstractCommonRestriction" package="restrictions" %} to do this e.g.

```java
public class MultipleOfRestriction extends AbstractCommonRestriction {

    private final int multipleOf;

    public MultipleOfRestriction(int multipleOf) {
        this.multipleOf = multipleOf;
    }

    private <T> void validate(ParseState<T> state, String optionTitle, Object value) {
        if (value instanceof Number) {
            Number n = (Number) value;
            if (n.longValue() % this.multipleOf != 0) {
                throw new ParseRestrictionViolatedException(
                        "Option '%s' must be an integer multiple of '%d' but got value '%s'", optionTitle,
                        this.multipleOf, n);
            }
        } else {
            throw new ParseRestrictionViolatedException(
                    "Option '%s' must be an integer multiple of '%d' which requires a numeric value but got value '%s'",
                    optionTitle, this.multipleOf, value);
        }
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option, Object value) {
        validate(state, option.getTitle(), value);
    }

    @Override
    public <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments, Object value) {
        validate(state, getArgumentTitle(state, arguments), value);
    }
}
```

In this example we create a general purpose private `validate()` method to enforce the restriction and call this from both of our overridden `postValidate()` methods.

When we detect a violation of our restriction we throw a `ParseRestrictionViolatedException` detailing the violation, depending on the restriction being implemented you might want to use one of its more specific sub-classes.  See [Exceptions](../practise/exceptions.html) for more details on the available exception classes.

### Implementation Methods

The methods you will need to `@Override` will depend on the kind of restriction we are implementing, for `@Arguments` and `@Option` restrictions there are three possible methods that you can override:

- `preValidate()`
- `postValidate()`
- `finalValidate()`

For some advanced restrictions you may want to implement multiple of these but most of the time you only need to implement one.

#### preValidate()

`preValidate()` is called when the parser has encountered the option/arguments **before** it has converted the provided raw value into a typed value.  So this can be used to enforce restrictions against the raw string values that users input e.g. [`@AllowedRawValues`](../annotations/allowed-raw-values.html) or [`@MinLength`](../annotations/min-length.html)

You will be passed the current parser state, the option/arguments metadata as appropriate and the `String` value.

#### postValidate()

`postValidate()` is called when the parser has encountered the option/arguments **after** it has converted the provided raw value into a typed value.  So this can be used to enforce restrictions against strongly typed values e.g. [`@AllowedValues`](../annotations/allowed-values.html) or [`@IntegerRange`](../annotations/integer-range.html)

You will be passed the current parser state, the option/arguments metadata as appropriate and the typed value as an `Object`.

#### finalValidate()

`finalValidate()` is called after all tokens have been parsed but before the parser returns the generated command to the caller.  Therefore this can be used to enforce restrictions that need to expect the final state of the parser e.g. [`@MutuallyExclusiveWith`](../annotations/mutually-exclusive-with.html) or [`@Required`](../annotations/required.html)

You will be passed the current parser state and the option/arguments metadata as appropriate.

## ServiceLoader Manifest

Airline detects and processes the available restrictions using Java's `ServiceLoader` mechanism for dynamic loading of extension points.  This requires manifest files to be placed under `META-INF/services` inside your JAR files, in a typical Maven build environment you will place these under `src/main/resources/META-INF/services` to ensure they are output in the correct place within the resulting JAR file.

You will need to add a manifest for each of the factory interfaces you choose to implement.  So for our example on this page we need to add two manifests:

- `com.github.rvesse.airline.restrictions.factories.ArgumentsRestrictionFactory`
- `com.github.rvesse.airline.restrictions.factories.OptionRestrictionFactory`

Each line in the manifest simply is the class name of our factory implementations, so for our example we would need to add the following line to both manifests:

```
com.github.rvesse.airline.examples.userguide.restrictions.custom.MultipleRestrictionFactory
```

{% include alert.html %}
`ServiceLoader` will scan all manifests it can find anywhere on the classpath so they may be present across multiple JARs on your classpath.

If you are using a build process that combines/repackages JARs (e.g. Maven Shade) please make sure that you are appropriately handling merging of these manifests as otherwise some restrictions may no longer be usable.
{% include end-alert.html %}

## Bonus Credit - Help Hints

If we want our restriction to tie into Airline's [Help System](../help/index.html) then we should also have our restriction implement the [`HelpHint`](../help/hints.html) interface.

Let's take a look at what this looks like for our example restriction:

```java
    @Override
    public String getPreamble() {
        return null;
    }

    @Override
    public HelpFormat getFormat() {
        return HelpFormat.PROSE;
    }

    @Override
    public int numContentBlocks() {
        return 1;
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        if (blockNumber != 0)
            throw new IndexOutOfBoundsException();
        return new String[] { String.format(
                "This options value must be a numeric value that is an integer multiple of %d", this.multipleOf) };
    }
```

Please see the [`HelpHint`](../help/hints.html) documentation for more detailed explanation of the methods you need to implement.  Essentially our implementation here provides a single paragraph describing our restriction in human readable terms.