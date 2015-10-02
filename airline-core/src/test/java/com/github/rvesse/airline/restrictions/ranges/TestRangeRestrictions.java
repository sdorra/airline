/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.restrictions.ranges;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.errors.ParseInvalidRestrictionException;
import com.github.rvesse.airline.parser.errors.ParseOptionOutOfRangeException;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.RangeRestriction;

public class TestRangeRestrictions {
    
    private void hasRangeRestriction(CommandMetadata metadata) {
        for (OptionMetadata option : metadata.getAllOptions()) {
            for (OptionRestriction restriction : option.getRestrictions()) {
                if (restriction instanceof RangeRestriction) return;
            }
        }
        Assert.fail("No RangeRestriction found");
    }

    @Test
    public void integer_range_inclusive() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        
        for (int i = 0; i <= 100; i++) {
            OptionRangeBase cmd = parser.parse("-i", Integer.toString(i));
            Assert.assertEquals(cmd.i, i);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value <= 100).*")
    public void integer_range_inclusive_below_min() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "-1");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value <= 100).*")
    public void integer_range_inclusive_above_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "101");
    }
    
    @Test
    public void integer_range_inclusive_min() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        hasRangeRestriction(parser.getCommandMetadata());
        
        for (int i = 0; i < 100; i++) {
            OptionRangeBase cmd = parser.parse("-i", Integer.toString(i));
            Assert.assertEquals(cmd.i, i);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value < 100).*")
    public void integer_range_inclusive_min_below_min() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "-1");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value < 100).*")
    public void integer_range_inclusive_min_at_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "100");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 <= value < 100).*")
    public void integer_range_inclusive_min_above_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMin.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "101");
    }
    
    @Test
    public void integer_range_inclusive_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        hasRangeRestriction(parser.getCommandMetadata());
        
        for (int i = 1; i <= 100; i++) {
            OptionRangeBase cmd = parser.parse("-i", Integer.toString(i));
            Assert.assertEquals(cmd.i, i);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 < value <= 100).*")
    public void integer_range_inclusive_max_below_min() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "-1");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 < value <= 100).*")
    public void integer_range_inclusive_max_at_min() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "0");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0 < value <= 100).*")
    public void integer_range_inclusive_max_above_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeInclusiveMax.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-i", "101");
    }
    
    @Test
    public void integer_range_single_value() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionIntegerRangeSingleValue.class);
        hasRangeRestriction(parser.getCommandMetadata());
        
        OptionRangeBase cmd = parser.parse("-i", "0");
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
    
    @Test
    public void double_range_inclusive() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionDoubleRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        
        for (double d = 0d; d <= 1.0d; d += 0.01d) {
            OptionRangeBase cmd = parser.parse("-d", Double.toString(d));
            Assert.assertEquals(cmd.d, d);
        }
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0.0 <= value <= 1.0).*")
    public void double_range_inclusive_below_min() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionDoubleRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-d", "-1.0");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(0.0 <= value <= 1.0).*")
    public void double_range_inclusive_above_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionDoubleRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-d", "1.01");
    }
    
    @Test
    public void lexical_range_inclusive() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionLexicalRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-s", "aardvark");
        parser.parse("-s", "bear");
        parser.parse("-s", "coyote");
        parser.parse("-s", "d");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(a <= value <= d).*")
    public void lexical_range_inclusive_above_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionLexicalRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-s", "deer");
    }
    
    @Test(expectedExceptions = ParseOptionOutOfRangeException.class, expectedExceptionsMessageRegExp = ".*(a <= value <= d).*")
    public void lexical_range_inclusive_below_max() {
        SingleCommand<? extends OptionRangeBase> parser = TestingUtil.singleCommandParser(OptionLexicalRangeInclusive.class);
        hasRangeRestriction(parser.getCommandMetadata());
        parser.parse("-s", "0");
    }
}
