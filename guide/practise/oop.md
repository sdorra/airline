---
layout: page
title: Inheritance and Composition
---

{% include toc.html %}

## Inheritance

When you define a class as being a [`@Command`](../annotations/command.html) Airline will automatically discover command metadata by examining the class hierarchy of the annotated class.  By this we mean that it will walk up the hierarchy to base classes to discover additional Airline annotations that are inherited by the command class.  This means that you can use standard inheritance to define base classes that contain [`@Option`](../annotations/option.html) definitions that can be inherited by mutliple command implementations.

For example we might want to have all our commands have a verbose option available e.g.

```java
public abstract class BaseCommand implements ExampleRunnable {

    @Option(name = { "-v", "--verbose" }, description = "Enables verbose mode")
    protected boolean verbose = false;
}

@Command(name = "maybe-verbose")
public abstract class MaybeVerboseCommand extends BaseCommand {

    @Override
    public int run() {
        if (this.verbose) {
            System.out.println("Verbose");
        } else {
            System.out.println("Normal");
        }
        return 0;
    }
    
    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(MaybeVerboseCommand.class, args);
    }
}
```

Note that we still need to follow normal inheritance best practises about field visibility, in the above example the field in the parent class is marked as `protected` so that we can access in the child class.

### Option Overriding

When we have larger class hierarchies it may be desirable to override the implementation of a option to be more specific to a given command.  Details on overriding options are given in the documentation of the [`@Option` Overriding](../annotations/option.html#overrides-and-sealed) annotation.

When you override an option Airline will still populate all the relevant fields individually.  So for example if you override an option in a child class both the parent and child field for that option will get populated if the user specifies the option.  This ensures that any logic in the parent and child can use the field values as they would usually.

#### Option Restrictions

If you are using [Restrictions](../restrictions/) then any restrictions defined on an option are controlled by the deepest definition of the option with restrictions.  This means that you can change the restrictions on an option by defining new restrictions on an override.  Equally if you don't define any restrictions with an override you automatically inherit any restrictions defined by the parent definition.

If you want to remove the restrictions on an overridden option you can use the special [`@Unrestricted`](../annotations/unrestricted.html) annotation to denote this.

### Help Section Inheritance

If you are using Help Annotations e.g. [`@Discussion`](../annotations/discussion.html) then any help sections defined are automatically inherited by child classes.  For example you might wish to define a [`@Copyright`](../annotations/copyright.html) annotation on your base class to automatically add a Copyright section to all your commands.

If the same help annotation is defined multiple times in a class hierarchy the deepest definition is used.  So if your parent defines an `@Copyright` annotation and your child class also defines an `@Copyright` annotation then your child definition will be used.

If you wish to hide an inherited help section the special [`@HideSection`](../annotations/hide-section.html) annotation can be used to do this.

## Composition

Additionally we may want to break out sets of related options into reusable modules and compose these together into our classes.  When Airline is scanning the command class for annotated fields it will also scan any field marked with the standard Java `@Inject` annotation.  The class for that field will be scanned and any further annotations included into the command metadata.  For example if we wanted to make our verbose option reusable across commands without any common ancestor we could do the following:

```java
public class VerbosityModule {

    @Option(name = { "-v", "--verbosity" }, arity = 1, title = "Level", description = "Sets the desired verbosity")
    // The AllowedRawValues annotation allows an option to be restricted to a given set of values
    @AllowedRawValues(allowedValues = { "1", "2", "3" })
    public int verbosity = 1;
}

@Command(name = "module-reuse", description = "A command that demonstrates re-use of modules and composition with locally defined options")
public class ModuleReuse implements ExampleRunnable {

    @Inject
    private HelpOption<ExampleRunnable> help;

    /**
     * A field marked with {@link Inject} will also be scanned for options
     */
    @Inject
    private VerbosityModule verbosity = new VerbosityModule();

    @Arguments
    private List<String> args = new ArrayList<String>();

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(ModuleReuse.class, args);
    }

    @Override
    public int run() {
        if (!help.showHelpIfRequested()) {
            System.out.println("Verbosity is " + verbosity.verbosity);
            System.out.println("Arguments were " + StringUtils.join(args, ", "));
        }
        return 0;
    }
}
```

Note that we are able to compose as many other classes as we want by defining multiple fields annotated with `@Inject`.  Note that when accessing these options we have to access them via their originating fields so again we need to be aware of field visibility when composing modules together.