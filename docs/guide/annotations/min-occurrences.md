---
layout: page
title: MinOccurrences Annotation
---

## `@MinOccurrences`

The `@MinOccurrences` annotation is used to indicate that an [`@Option`](option.html) or [`@Arguments`](arguments.html) may be specified at least some number of times.

```java
@Option(name = "--name",
        arity = 1,
        title = "Name")
@MinOccurrences(occurrences = 2)
private List<String> name;
```

Here we specify that the `--name` option must be provided at least twice.  If the option is not provided enough times an error will be thrown during parsing.

## Related Annotations

To specify the maximum number of occurrences use the [`@MaxOccurrences`](max-occurrences.html) annotation.

If you want an option to occur only once then use the [`@Once`](once.html) annotation, if you want to require that the option occur then use the [`@Required`](required.html) annotation.