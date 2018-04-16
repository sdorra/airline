---
layout: page
title: Groups Annotation
---

## `@Groups`

The `@Groups` annotation is used to specify group information.  This may be done as an alternative or in addition to using the relevant fields of the [`@Command`](command.html) or [`@Cli`](cli.html) annotations to specify groups.

This annotation should be used when you wish to make your `@Command` annotated class part of multiple groups since you can't directly place multiple [`@Group`](group.html) annotations on a class.

{% include alert.html %}
Generally we recommend that you use the `groups` field of the [`@Cli`](cli.html) annotation to specify groups however this annotation can be useful if you are dynamically constructing your CLI and want to allow individual commands to specify their group memberships.
{% include end-alert.html %}

### Use on an `@Command` class

For example:

```java
@Command(name = "group-member")
@Groups({
     @Group(name = "advanced",
            defaultCommand = GroupMember.class,
            commands = { Tool.class }),
      @Group(name = "other",
             commands = { GroupMember.class })
})
public class GroupMember { }
```

Here we use the `@Groups` annotation to place our command in both the `advanced` group and the `other` group.  Please see the [`@Group`](group.html) page for more information on using that annotation.