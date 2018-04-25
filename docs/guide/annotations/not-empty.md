---
layout: page
title: NotEmpty Annotation
---

## `@NotEmpty`

The `@NotEmpty` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to require that the value given not be an empty string e.g.

```java
@Option(name = "--reference", arity = 1)
@NotEmpty
private String reference;
```

Restricts the `--reference` option to strings which are not empty strings

### Related Annotations

If you want to require a value that is not all whitespace then [`@NotBlank`](not-blank.html) would be suitable.