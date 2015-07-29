package com.github.rvesse.airline.restrictions;

import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;

public class TestOccurrences {

    @Test
    public void occurrences_good() {
        SingleCommand<Occurrences> parser = TestingUtil.singleCommandParser(Occurrences.class);
        parser.parse("-a", "a1", "-b", "b1", "-b", "b2");
        parser.parse("-a", "a1", "-a", "a2", "-a", "a3", "-b", "b1", "-b", "b2", "-c", "c1");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class, expectedExceptionsMessageRegExp = ".*(maximum of 3).*(found 4).*")
    public void occurrences_bad_too_many_01() {
        SingleCommand<Occurrences> parser = TestingUtil.singleCommandParser(Occurrences.class);
        parser.parse("-a", "a1", "-a", "a2", "-a", "a3", "-a", "a4");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class, expectedExceptionsMessageRegExp = ".*(maximum of 1).*(found 2).*")
    public void occurrences_bad_too_many_02() {
        SingleCommand<Occurrences> parser = TestingUtil.singleCommandParser(Occurrences.class);
        parser.parse("-c", "c1", "-c", "c2");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class, expectedExceptionsMessageRegExp = ".*(at least 2).*(found 0).*")
    public void occurrences_bad_too_few_01() {
        SingleCommand<Occurrences> parser = TestingUtil.singleCommandParser(Occurrences.class);
        parser.parse();
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class, expectedExceptionsMessageRegExp = ".*(at least 2).*(found 1).*")
    public void occurrences_bad_too_few_02() {
        SingleCommand<Occurrences> parser = TestingUtil.singleCommandParser(Occurrences.class);
        parser.parse("-b", "b1");
    }
}
