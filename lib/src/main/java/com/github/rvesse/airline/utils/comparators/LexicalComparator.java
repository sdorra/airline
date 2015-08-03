package com.github.rvesse.airline.utils.comparators;

import java.text.Collator;
import java.util.Locale;

public class LexicalComparator extends AbstractObjectComparator<String> {
    
    private final Collator collator;

    public LexicalComparator(Locale locale) {
        super(String.class);
        this.collator = Collator.getInstance(locale);
    }

    @Override
    protected int compareValues(String v1, String v2) {
        return this.collator.compare(v1, v2);
    }

}
