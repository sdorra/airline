---
layout: page
title: Supported Types
---

Airline converts the string arguments that users pass into the JVM at the command line into the appropriate Java types using a `TypeConverter` and uses these values to populate `@Option` and `@Arguments` annotated fields.

## Default Type Converter

The `TypeConverter` may be specified as part of the [Parser Configuration](../parser/) but if not explicitly configured will use the `DefaultTypeConverter`.  Out of the box this will support most common types, any Java `enum` and any Java class that conforms to certain constraints.

### Common Types

All of the following common Java types and any dervied type that may be assigned to them as determined by `Class.isAssignableFrom()` are supported:

- `String`
- `Boolean`
- `Byte`
- `Short`
- `Integer`
- `Long`
- `Float`
- `Double`

### `enum`

Any Java `enum` is automatically supported because the Java compiler provides a static `valueOf(String)` method that Airline can invoke to turn the string argument into an `enum` member.

### Java Classes

Similarly any other Java class that provides a static `valueOf(String)` can also have a string argument converted into a class instance in this way.

More generally Airline supports any Java class that meets one of the following constraints:

- Provides a static `valueOf(String)` method
- Provides a static `fromString(String)` method
- Provides a constructor which takes a single `String` argument

### Collections

Airline also supports any type which is a `Collection` of another supported type.

The advantage of using collection types e.g. `List<String>` is that your `@Option` or `@Arguments` annotated field stores all the values passed in.  If your field has a non-collection type then only the last use of that option/argument will be stored in the final class that Airline creates.

## Custom Type Converters

**TODO Document creating custom type converters**