/**
 * Copyright (C) 2010-16 the original author or authors.
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
package com.github.rvesse.airline.restrictions;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;

public class TestStrings {

    private SingleCommand<Strings> parser() {
        return TestingUtil.singleCommandParser(Strings.class);
    }
    
    @Test
    public void not_empty_valid() {
        Strings cmd = parser().parse("--not-empty", "foo");
        Assert.assertEquals(cmd.notEmpty, "foo");
    }
    
    @Test
    public void not_empty_valid_blank() {
        Strings cmd = parser().parse("--not-empty", " ");
        Assert.assertEquals(cmd.notEmpty, " ");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void not_empty_invalid() {
        parser().parse("--not-empty", "");
    }
    
    @Test
    public void not_blank_valid() {
        Strings cmd = parser().parse("--not-blank", "foo");
        Assert.assertEquals(cmd.notBlank, "foo");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void not_blank_invalid() {
        parser().parse("--not-blank", "");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void not_blank_invalid_blank() {
        parser().parse("--not-blank", " ");
    }
    
    @Test
    public void pattern_tel_valid() {
        Strings cmd = parser().parse("--tel", "555-123-4567");
        Assert.assertEquals(cmd.tel, "555-123-4567");
    }
    
    @Test
    public void pattern_tel_valid_prefixed() {
        Strings cmd = parser().parse("--tel", "+1-555-123-4567");
        Assert.assertEquals(cmd.tel, "+1-555-123-4567");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void pattern_tel_invalid() {
        parser().parse("--tel", "foo");
    }
    
    @Test
    public void min_length_valid() {
        Strings cmd = parser().parse("--min", "foobar");
        Assert.assertEquals(cmd.minLength, "foobar");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void min_length_invalid() {
        parser().parse("--min", "foo");
    }
    
    @Test
    public void max_length_valid() {
        Strings cmd = parser().parse("--max", "foo");
        Assert.assertEquals(cmd.maxLength, "foo");
    }
    
    @Test(expectedExceptions = ParseRestrictionViolatedException.class)
    public void max_length_invalid() {
        parser().parse("--max", "foobar");
    }
}
