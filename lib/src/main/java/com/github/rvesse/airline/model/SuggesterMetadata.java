package com.github.rvesse.airline.model;

import com.github.rvesse.airline.Accessor;
import com.github.rvesse.airline.help.Suggester;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class SuggesterMetadata
{
    private final Class<? extends Suggester> suggesterClass;
    private final List<Accessor> metadataInjections;

    public SuggesterMetadata(Class<? extends Suggester> suggesterClass, List<Accessor> metadataInjections)
    {
        this.suggesterClass = suggesterClass;
        this.metadataInjections = ImmutableList.copyOf(metadataInjections);
    }

    public Class<? extends Suggester> getSuggesterClass()
    {
        return suggesterClass;
    }

    public List<Accessor> getMetadataInjections()
    {
        return metadataInjections;
    }
}
