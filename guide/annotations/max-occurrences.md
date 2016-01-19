---
layout: page
title: MaxOccurrences Annotation
---

## `@MaxOccurrences`

The `@MaxOccurrences` annotation is used to indicate that an [`@Option`](option.html) or [`@Arguments`](arguments.html) may be specified at most some number of times.

```java
@Option(name = "--name",
        arity = 1,
        title = "Name")
@MaxOccurrences(occurrences = 2)
private List<String> name;
```

Here we specify that the `--name` option can be provided at most twice.  If the option is provided too many times then an error will be thrown during parsing.


## Related Annotations

To specify the minimum number of occurrences use the [`@MinOccurrences`](max-occurrences.html) annotation.  You can use both this and [`@MinOccurrences`](max-occurrences.html) to set both a minimum and maximum occurrences if you wish.

If you want an option to occur at most once then use the [`@Once`](once.html) annotation, if you want to require that the option occur then use the [`@Required`](required.html) annotation.