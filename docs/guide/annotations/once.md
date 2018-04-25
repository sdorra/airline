---
layout: page
title: Once Annotation
---

## `@Once`

The `@Once` annotation is used to indicate that an [`@Option`](option.html) or [`@Arguments`](arguments.html) may be specified at most once.

```java
@Option(name = "--name",
        arity = 1,
        title = "Name")
@Once
private String name;
```

Here we specify that the `--name` option may only be provided once.  If the user specifies it more than once then an error will be thrown during parsing.


## Related Annotations

Since this acts as a maximum the user is not required to specify the option, if you want to require that the option occur then use the [`@Required`](required.html) annotation.

To specify the minimum number of occurrences you can use the [`@MinOccurrences`](min-occurrences.html) annotation or for maximum number of occurrences the [`@MaxOccurrences`](max-occurrences.html) annotation.