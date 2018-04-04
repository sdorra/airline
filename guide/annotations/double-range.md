---
layout: page
title: DoubleRange Annotation
---

## `@DoubleRange`

The `@DoubleRange` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to limit the set of values that an option may be used with to a range of `double` values e.g.

```java
@Option(name = "--opacity", title = "Opacity", version = "Desired opacity")
@DoubleRange(min = 0.0, minInclusive = true, max = 1.0, maxInclusive = true)
public double opacity;
```
This specifies that the `--opacity` option only allows values in the range `0.0` through `1.0` to be specified by the user.  Any other value will be rejected.

### Advanced Ranges

The `minInclusive` and `maxInclusive` fields of the annotation specify whether the given `min` and `max` are included in the range or not.

The `min` and `max` automatically default to the minimum and maximum for the corresponding type e.g. `Double.MAX_VALUE` so there is no need to specify these explicitly if you simply wish to specify a minimum or maximum value only.

### Related Annotations

For other numeric types there are equivalent range restriction annotations: [`@ByteRange`](byte-range.html), [`@FloatRange`](float-range.html), [`@IntegerRange`](integer-range.html), [`@LongRange`](long-range.html) and [`@ShortRange`](short-range.html).  There is also a [`@LexicalRange`](lexical-range.html) for string ranges.

If there is a set of non-contiguous values that should be considered acceptable then use the [`@AllowedValues`](allowed-values.html) annotation instead.

For more complex value restrictions a regular expression based restriction using [`@Pattern`](pattern.html) might be appropriate.