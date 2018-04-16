---
layout: page
title: NotBlank Annotation
---

## `@NotBlank`

The `@NotBlank` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to require that the value given not be all whitespace e.g.

```java
@Option(name = "--reference", arity = 1)
@NotBlank
private String reference;
```

Restricts the `--reference` option to strings which are not entirely whitespace i.e. this must be some non-whitespace characters in the value

### Related Annotations

If you want to require a non-empty value then [`@NotEmpty`](not-empty.html) would be suitable.