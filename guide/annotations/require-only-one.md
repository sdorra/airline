---
layout: page
title: RequireOnlyOne Annotation
---

## `@RequireOnlyOne`

The `@RequireOnlyOne` annotation is applied to a field annotated with [`@Option`](option.html) to indicate that exactly one from some set of options must be specified e.g.

```java
@Option(name = "--num", 
        arity = 1, 
        title = "Number")
@RequireOnlyOne(tag = "identifier")
private int number;

@Option(name = "--name",
        arity = 1,
        title = "Name")
@RequireOnlyOne(tag = "identifier")
```

When fields are marked with `@RequireOnlyOne` if the user fails to supply exactly one of the options that have the same `tag` value then an error will be thrown during [parsing](../parser/).

In this example the user must specify exactly one of the `--num` or `--name` option, if they specify neither or specify both then it is treated as an error.

### Related Annotations

If you want to require that at least one of some set of options be specified then you should use [`@RequireSome`](require-some.html) instead.

If you optionally want to allow only one from some set of options then you should use [`@MutuallyExclusiveWith`](mutually-exclusive-with.html).