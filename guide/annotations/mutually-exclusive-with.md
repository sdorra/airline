---
layout: page
title: MutuallyExclusiveWith Annotation
---

## `@MutuallyExclusiveWith`

The `@MutuallyExclusiveWith` annotation is applied to a field annotated with [`@Option`](option.html) to indicate that exactly one from some set of options may be specified e.g.

```java
@Option(name = "--num", 
        arity = 1, 
        title = "Number")
@MutuallyExclusiveWith(tag = "identifier")
private int number;

@Option(name = "--name",
        arity = 1,
        title = "Name")
@MutuallyExclusiveWith(tag = "identifier")
private String name;
```

When fields are marked with `@MutuallyExclusiveWith` if the user specifies more than one of the options that have the same `tag` value then an error will be thrown during [parsing](../parser/).

In this example the user may specify exactly one of the `--num` or `--name` option or they may specify neither. If they specify both then it is treated as an error.

### Related Annotations

If you want to require that at least one of some set of options be specified then you should use [`@RequireSome`](require-some.html) instead.

If you want to require exactly one from some set of options then you should use [`@RequireOnlyOne`](require-only-one.html).