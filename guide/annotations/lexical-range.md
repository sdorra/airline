---
layout: page
title: LexicalRange Annotation
---

## `@LexicalRange`

The `@LexicalRange` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to limit the set of values that an option may be used with to a range of `String` values e.g.

```java
@Option(name = "--category", title = "Category")
@LexicalRange(min = "A", max = "Z")
public String ;
```
This specifies that the `--cateogry` option only allows values in the range `A` through `Z` to be specified by the user.  Any other value will be rejected.

So for example `Aeroplanes` would be valid since it is greater than the minimum, however `Zoo` would not as it is officially past.

Note that the ordering uses the lexical ordering rules of a locale, if not explicitly specified it uses the `en` i.e. English locale.  You can customise the locale used as described under Advanced Ranges below.

### Advanced Ranges

The `minInclusive` and `maxInclusive` fields of the annotation specify whether the given `min` and `max` are included in the range or not.

The `min` and `max` automatically default to the minimum and maximum for the corresponding type, in the case of strings this is taken to be `null` i.e. no minimum/maximum.  So there is no need to specify these explicitly if you simply wish to specify a minimum or maximum value only.

The `locale` field can be used to specify a BCP-47 language tag to specify the locale used to compare whether the strings occur in the range e.g.

```java
@Option(name = "--category", title = "Category")
@LexicalRange(min = "A", max = "Z", locale = "pl")
public String ;
```

Would do the comparison using Polish lexical ordering.

### Related Annotations

For other numeric types there are equivalent range restriction annotations: [`@ByteRange`](byte-range.html), [`@DoubleRange`](double-range.html), [`@FloatRange`](float-range.html), [`@IntegerRange`](integer-range.html), [`@LongRange`](long-range.html) and [`@ShortRange`](short-range.html).

For more complex value restrictions a regular expression based restriction using [`@Pattern`](pattern.html) might be appropriate.