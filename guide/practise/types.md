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

To create a custom type converter that converts strings to Java objects in some other way(s) you will need to implement the `TypeConverter` interface.  This interface has a single method with the following signature:

```java
Object convert(String name, Class<?> type, String value);
```

Where `name` is the name of the option/argument we are trying to convert a value for, `type` is the target type to which we are trying to convert and `value` is the string value we are converting.

Often it may be easier to simply extend the default behaviour described on this page by extending the `DefaultTypeConverter` e.g.

```java
package com.github.rvesse.airline.examples.userguide.practise;

import com.github.rvesse.airline.DefaultTypeConverter;

/**
 * An example of an extended type converter that adds support for converting
 * from types that provide an {@code parse(String)} method
 *
 */
public class ExtendedTypeConverter extends DefaultTypeConverter {

    @Override
    public Object convert(String name, Class<?> type, String value) {
        checkArguments(name, type, value);

        // Try and convert from a parse(String) method
        ConvertResult result = this.tryConvertStringMethod(type, value, "parse");
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        // Fall back to default behaviour otherwise
        return super.convert(name, type, value);
    }
}
```

Here we define our `ExtendedTypeConverter` which overrides the `convert()` method to add an attempt to convert the type by looking for a `parse(String)` method on the type.  `ConvertResult` is a helper class used in `DefaultTypeConverter` to pass around the results of an attempted conversion.