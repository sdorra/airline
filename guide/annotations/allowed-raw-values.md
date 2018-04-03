---
layout: page
title: AllowedRawValues Annotation
---

## `@AllowedRawValues`

The `@AllowedRawValues` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to limit the set of values that an option may be used with e.g.

```
java
@Option(name = { "-v", "--verbosity" }, arity = 1, title = "Level", description = "Sets the desired verbosity")
@AllowedRawValues(allowedValues = { "1", "2", "3" })
public int verbosity = 1;
```
This specifies that the `--verbosity` option only allows the values `1`, `2` and `3` to be specified by the user.  Any other value will be rejected.

Note that the restriction applies to the raw string provided to the parser **prior** to converting it to the target type.  So in the above example the values would be checked prior to parsing them as integers.

### Related Annotations

If there may be more than one way that the same value may be specified then it may be useful to use the [`@AllowedValues`](allowed-values.html) annotation instead.