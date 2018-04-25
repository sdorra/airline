---
layout: page
title: Restrictions
---

Airline includes a powerful and extensible restrictions system that allows users to significantly reduce the boiler plate code typical of many CLI libraries.  Restrictions work by simply applying appropriate annotations to the [`@Option`](../annotations/option.html) or [`@Arguments`](../annotations/arguments.html) annotated fields that you wish to restrict and Airline handles all the work of enforcing those restrictions for you.

For example here's a class representing a UK postal address annotated with appropriate restrictions:

```java
public class PostalAddress {
    
    @Option(name = "--recipient", 
            title = "Recipient", 
            description = "Specifies the name of the receipient")
    @Required
    public String recipient;

    @Option(name = "--number", 
            title = "HouseNumber", 
            description = "Specifies the house number")
    @RequireOnlyOne(tag = "nameOrNumber")
    @IntegerRange(min = 0, minInclusive = false)
    public Integer houseNumber;
    
    @Option(name = "--name", 
            title = "HouseName", 
            description = "Specifies the house name")
    @RequireOnlyOne(tag = "nameOrNumber")
    @NotEmpty
    @NotBlank
    public String houseName;
    
    @Option(name = { "-a", "--address", "--line" }, 
            title = "AddressLine", 
            description = "Specifies an address line.  Specify this multiple times to provide multiple address lines, these should be in the order they should be used.")
    @Required
    @MinOccurrences(occurrences = 1)
    public List<String> addressLines = new ArrayList<>();
    
    @Option(name = "--postcode",
            title = "PostCode", 
            description = "Specifies the postcode")
    @Required
    @Pattern(pattern = "^([A-Z]{1,2}([0-9]{1,2}|[0-9][A-Z])) (\\d[A-Z]{2})$", description = "Must be a valid UK postcode.", flags = java.util.regex.Pattern.CASE_INSENSITIVE)
    public String postCode;
}
```

Here we can see a variety of different restrictions annotations being used.  For example we use the [`@RequireOnlyOne`](../annotations/require-only-one.html) annotation to require that either a house name or number must be provided.  The [`@MinOccurrences`](../annotations/min-occurrences.html) annotation to require at least one address line and the [`@Pattern`](../annotations/pattern.html) annotation to enforce postcode validation.

If you've ever written a command line application you can quickly see just how much boilerplate code you are avoiding having to write here.

As an additional benefit restrictions automatically interact with the [Help system](../help/index.html) so that option help automatically describes the restrictions that are applicable.  Below is the generated help from an example command that uses the above options module:

```
NAME
        ship-it check-address - Check if an address meets our restrictions

SYNOPSIS
        ship-it check-address {-a | --address | --line} <AddressLine>...
                [ --name <HouseName> ] [ --number <HouseNumber> ] --postcode <PostCode>
                --recipient <Recipient>

OPTIONS
        -a <AddressLine>, --address <AddressLine>, --line <AddressLine>
            Specifies an address line. Specify this multiple times to provide
            multiple address lines, these should be in the order they should be
            used.

            This option must occur a minimum of 1 times

        --name <HouseName>
            Specifies the house name

            This options value cannot be blank (empty or all whitespace)

            This options value cannot be empty

            This option is part of the group 'nameOrNumber' from which only one
            option may be specified

        --number <HouseNumber>
            Specifies the house number

            This options value must fall in the following range: value >0

            This option is part of the group 'nameOrNumber' from which only one
            option may be specified

        --postcode <PostCode>
            Specifies the postcode

            This options value must match the regular expression
            '^([A-Z]{1,2}([0-9]{1,2}|[0-9][A-Z])) (\d[A-Z]{2})$'. Must be a
            valid UK postcode.

        --recipient <Recipient>
            Specifies the name of the receipient
```

## Available Restrictions

{% include restrictions.md path="../annotations/" %}

## Custom Restrictions

Airline includes the ability to create [Custom Restrictions](custom.html) which allow you to define and use your own restriction annotations.