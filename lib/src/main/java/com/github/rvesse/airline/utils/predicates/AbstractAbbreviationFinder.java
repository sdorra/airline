package com.github.rvesse.airline.utils.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Predicate;

public abstract class AbstractAbbreviationFinder<T> implements Predicate<T> {

    private List<T> candidates = new ArrayList<T>();
    private T exact = null;

    public AbstractAbbreviationFinder(String value, Collection<T> items) {
        this.candidates.addAll(items);
        for (int i = 0; i < this.candidates.size(); i++) {
            T item = this.candidates.get(i);
            if (this.isExactNameMatch(value, item)) {
                this.exact = item;
                continue;
            }
            if (this.isPartialNameMatch(value, item))
                continue;
            this.candidates.remove(i);
            i--;
        }
    }
    
    protected abstract boolean isExactNameMatch(String value, T item);
    
    protected abstract boolean isPartialNameMatch(String value, T item);

    @Override
    public final boolean evaluate(T item) {
        return isExact(item) || isAbbreviation(item);
    }

    private boolean isExact(T item) {
        return this.exact != null && this.exact.equals(item);
    }

    private boolean isAbbreviation(T item) {
        return this.candidates.size() == 1 && this.candidates.contains(item);
    }
}
