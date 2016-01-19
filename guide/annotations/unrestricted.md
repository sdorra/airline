---
layout: page
title: Unrestricted Annotation
---

## `@Unrestricted`

The `@Unrestricted` annotation is applied to fields annotated with [`@Option`](option.html) or [`@Arguments`](arguments.html) to indicate that no restrictions should apply.

This is useful because by default restrictions are inherited so if you wish to remove restrictions when overriding an option definition then you need to use this annotation.

For example consider we have the following command defined:

```java
package com.github.rvesse.airline.examples.userguide.restrictions;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "required")
public class RequiredOption {

    @Option(name = "--name", 
            arity = 1, 
            title = "Name")
    @Required
    private String name;
    
    public static void main(String[] args) {
        SingleCommand.singleCommand(RequiredOption.class).parse(args);
    }
}
```

This command requires that the `--name` option be specified otherwise an error will be thrown.

However if we wanted to make that option optional in a derived class we can do so like so:

```java
package com.github.rvesse.airline.examples.userguide.restrictions;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Unrestricted;

@Command(name = "optional")
public class OptionalOption extends RequiredOption {

    @Option(name = "--name", 
            arity = 1, 
            title = "Name")
    @Unrestricted
    private String name;
    
    public static void main(String[] args) {
        SingleCommand.singleCommand(OptionalOption.class).parse(args);
    }
}
```

Here we use `@Unrestricted` to indicate that the `--name` option has no restrictions upon it.