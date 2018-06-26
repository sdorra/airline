---
layout: page
title: Partials Annotation
---

## `@Partials` and `@Partial`

These special annotations are used in conjunction with fields annotated with [`@Option`](option.html)/[`@Arguments`](arguments.html) to limit the effect of applied restriction annotations to only certain values.  This is only useful when you have options/arguments with arity &gt; 1 i.e. multiple values can be specified.

###  `@Partial`

For example consider the case of an option that takes in a key value pair, we might want to restrict the keys but still allow arbitrary values e.g.

```java
@Command(name = "partial")
public class PartialAnnotated {

    @Option(name = "--kvp", arity = 2)
    @Partial(appliesTo = { 0 }, restriction = NotBlank.class)
    @NotBlank
    public List<String> kvps = new ArrayList<>();
 }
```

So here we have a field annotated with [`@NotBlank`](not-blank.html) which would normally apply to both the two values this option expects.

However by using `@Partial` we are able to modify the restriction to only apply to the first value.  The `appliesTo` field is used to state which values the restriction applies to using a zero based index and the `restriction` field states the corresponding restriction that is modified.

### `@Partials`

In the case where we want to apply different restrictions to different values we would need to use the `@Partials` annotation in order to specify multiple `@Partial` annotations like so:

```java
@Command(name = "partial")
public class PartialsAnnotated {

    @Option(name = "--kvp", arity = 2)
    @Partials({
        @Partial(appliesTo = { 0 }, restriction = AllowedRawValues.class),
        @Partial(appliesTo = { 1 }, restriction = NotBlank.class)
    })
    @AllowedRawValues(allowedValues = { "client", "server", "security" })
    @NotBlank
    public List<String> kvps = new ArrayList<>();
```

Here we apply the [`@AllowedRawValues`](allowed-raw-values.html) restriction to the first value and the `@NotBlank` restriction to the second value.  This allows us to restrict the keys to a set of known keys and allow any non-blank value for the value.

### Limitations

One limitation of partial restrictions are that they don't make any sense in the event where you have a restriction that needs to inspect the entire parser state e.g. [`@MutuallyExclusiveWith`](mutually-exclusive-with.html).  In those cases modifying those restrictions with `@Partial` actually has the effect of removing the restriction.