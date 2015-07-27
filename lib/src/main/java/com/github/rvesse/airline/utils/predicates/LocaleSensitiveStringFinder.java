package com.github.rvesse.airline.utils.predicates;

import java.text.Collator;
import java.util.Locale;

import org.apache.commons.collections4.Predicate;

public class LocaleSensitiveStringFinder implements Predicate<String> {

    private final String value;
    private final Collator collator;
    
    public LocaleSensitiveStringFinder(String value, Locale locale) {
        if (locale == null) throw new NullPointerException("locale cannot be null");
        this.value = value;
        this.collator = Collator.getInstance(locale);
    }

    @Override
    public boolean evaluate(String str) {
        return this.collator.compare(this.value, str) == 0;
    }
}
