---
layout: page
title: Port Annotation
---

## `@Port`

The `@Port` annotation may be applied to fields annotated with [`@Option`](option.html) and [`@Arguments`](arguments.html) to restrict values which are intended to reference a port number e.g.

```java
@Option(name = "-p", title = "Port")
@Port(acceptablePorts = { PortType.OS_ALLOCATED, PortType.DYNAMIC })
public int port;
```
This restricts the `-p` option to taking a value which is either an `OS_ALLOCATED` or `DYNAMIC` port which corresponds to the ranges `0` and `49512` through `65535`.

The following table shows the available port ranges:

| `PortType` constant | Port Ranges | Notes |
| -------------------------- | ----------------- | -------- |
| `ANY` | `0` through `65535` | Any valid port number |
| `OS_ALLOCATED` | `0` | The system will allocate a free port since `0` is not a real port number but a special constant |
| `SYSTEM` | `1` through `1023` | System ports, usually require administrative privileges to listen on |
| `USER` | `1024` through `49151` | User ports that may be registered with the IANA |
| `DYNAMIC` | `49152` through `65535` | Dynamic aka `ephermeral` ports |

### Related Annotations

If you have a more restrictive set of acceptable ports to apply then the [`@IntegerRange`](integer-range.html) annotation may be better suited though that only permits a single range to be specified.