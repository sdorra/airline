package com.github.rvesse.airline.parser.errors;

import java.util.Set;

public class ParseAliasCircularReferenceException extends ParseException {

    public ParseAliasCircularReferenceException(String string, Set<String> referenceChain, Object[] args) {
        super(string, args);
        // TODO Auto-generated constructor stub
    }

}
