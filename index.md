---
layout: default
title: Airline
permalink: index.html
---

# Airline

Airline is an annotation-driven Java library for building Command Line Interfaces (CLIs), it supports simple commands all the way through to complex Git style CLIs with groups and user defined command aliases.

Airline aims to reduce the boiler plate code typically associated with CLIs in Java, many common behaviours can be achieved purely with annotations and zero user code.  Let's take a look at an ultra simple example:

## How to Use

Please start reading the [User Guide](guide/) to learn how to use Airline for your applications.

## Get Airline

You can get Airline from Maven central by specifying the following Maven coordinates:

```xml
<dependency>
  <groupId>com.github.rvesse</groupId>
  <artifactId>airline</artifactId>
  <version>X.Y.Z</version>
</dependency>
```

Where `X.Y.Z` is your desired version, the current stable release is `{{ site.version }}`

## License

Airline is open source software licensed under the [Apache License 2.0](http://apache.org/licenses/LICENSE-2.0)

Please see `license.txt` in this repository for further details

## Acknowledgements

This project was forked from [http://github.com/airlift/airline](http://github.com/airlift/airline) and would not exist at all were it not for that library.

This website is built with [Jekyll](http://jekyllrb.com), it uses the [Hyde theme](https://github.com/poole/hyde) by Mark Otto and the [Table of Contents plugin](https://github.com/ghiculescu/jekyll-table-of-contents) by Alex Ghiculescu both of which are under the MIT License