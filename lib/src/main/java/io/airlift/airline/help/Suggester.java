package io.airlift.airline.help;

public interface Suggester
{
    Iterable<String> suggest();
}
