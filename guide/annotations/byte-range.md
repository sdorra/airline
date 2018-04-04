---
layout: page
title: ByteRange Annotation
---

## `@ByteRange`

The `@ByteRange` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to limit the set of values that an option may be used with to a range of `byte` values e.g.

```java
@Option(name = "--version", title = "Version", version = "Version to use")
@LongRange(min = 1, minInclusive = true, max = 5, maxInclusive = true)
public byte version;
```
This specifies that the `--version` option only allows values in the range `1` through `5` to be specified by the user.  Any other value will be rejected.


### Related Annotations

For other numeric types there are equivalent range restriction annotations: [`@DoubleRange`](double-range.html), [`@FloatRange`](float-range.html), [`@IntegerRange`](integer-range.html), [`@LongRange`](long-range.html) and [`@ShortRange`](short-range.html).  There is also a [`@LexicalRange`](lexical-range.html) for string ranges.

If there is a set of non-contiguous values that should be considered acceptable then use the [`@AllowedValues`](allowed-values.html) annotation instead.

For more complex value restrictions a regular expression based restriction using [`@Pattern`](pattern.html) might be appropriate.