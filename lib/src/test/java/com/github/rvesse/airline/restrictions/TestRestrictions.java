package com.github.rvesse.airline.restrictions;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.parser.errors.ParseInvalidRestrictionException;
import com.github.rvesse.airline.parser.errors.ParseOptionOutOfRangeException;

public class TestRestrictions {

    @Test
    public void integer_range_inclusive() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusive.class);
        
        for (int i = 0; i <= 100; i++) {
            OptionInteger cmd = parser.parse("-i", Integer.toString(i));
            Assert.assertEquals(cmd.i, i);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value <= 100).*")
    public void integer_range_inclusive_below_min() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusive.class);
        parser.parse("-i", "-1");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value <= 100).*")
    public void integer_range_inclusive_above_max() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusive.class);
        parser.parse("-i", "101");
    }
    
    @Test
    public void integer_range_inclusive_min() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        
        for (int i = 0; i < 100; i++) {
            OptionInteger cmd = parser.parse("-i", Integer.toString(i));
            Assert.assertEquals(cmd.i, i);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value < 100).*")
    public void integer_range_inclusive_min_below_min() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        parser.parse("-i", "-1");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value < 100).*")
    public void integer_range_inclusive_min_at_max() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        parser.parse("-i", "100");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value < 100).*")
    public void integer_range_inclusive_min_above_max() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        parser.parse("-i", "101");
    }
    
    @Test
    public void integer_range_inclusive_max() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        
        for (int i = 1; i <= 100; i++) {
            OptionInteger cmd = parser.parse("-i", Integer.toString(i));
            Assert.assertEquals(cmd.i, i);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 < value <= 100).*")
    public void integer_range_inclusive_max_below_min() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        parser.parse("-i", "-1");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 < value <= 100).*")
    public void integer_range_inclusive_max_at_min() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        parser.parse("-i", "0");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 < value <= 100).*")
    public void integer_range_inclusive_max_above_max() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        parser.parse("-i", "101");
    }
    
    @Test
    public void integer_range_single_value() {
        SingleCommand<? extends OptionInteger> parser = TestingUtil.singleCommandParser(OptionIntegerRangeSingleValue.class);
        OptionInteger cmd = parser.parse("-i", "0");
        Assert.assertEquals(cmd.i, 0);
    }
    
    @Test(expectedExceptions = ParseInvalidRestrictionException.class)
    public void integer_range_invalid_01() {
        TestingUtil.singleCommandParser(OptionIntegerRangeInvalid.class);
    }
    
    @Test(expectedExceptions = ParseInvalidRestrictionException.class)
    public void integer_range_invalid_02() {
        TestingUtil.singleCommandParser(OptionIntegerRangeInvalidSingleValue.class);
    }
}
