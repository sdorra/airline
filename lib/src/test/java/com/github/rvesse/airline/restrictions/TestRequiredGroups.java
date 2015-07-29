package com.github.rvesse.airline.restrictions;

import org.apache.commons.collections4.IteratorUtils;
import org.testng.annotations.Test;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.TestingUtil;
import com.github.rvesse.airline.parser.errors.ParseOptionGroupException;


public class TestRequiredGroups {
    
    @Test
    public void require_some_good() {
        SingleCommand<Some> parser = TestingUtil.singleCommandParser(Some.class);
        parser.parse("-a", "test");
        parser.parse("-b", "test");
        parser.parse("-c", "test");
        parser.parse("-a", "test", "-b", "test2");
        parser.parse("-a", "test", "-a", "test2");
        parser.parse("-a", "test", "-c", "test2");
        parser.parse("-b", "test", "-c", "test2");
        parser.parse("-a", "test", "-b", "test2", "-c", "test3");
        parser.parse("-a", "test", "-b", "test2", "-c", "test3", "-a", "test4", "-c", "test5");
    }
    
    @Test(expectedExceptions = ParseOptionGroupException.class)
    public void require_some_bad_none() {
        SingleCommand<Some> parser = TestingUtil.singleCommandParser(Some.class);
        parser.parse();
    }
    
    @Test
    public void require_one_good() {
        SingleCommand<One> parser = TestingUtil.singleCommandParser(One.class);
        parser.parse("-a", "test");
        parser.parse("-b", "test");
        parser.parse("-c", "test");
        parser.parse("-a", "test", "-a", "test2");
        parser.parse("-b", "test", "-b", "test2");
        parser.parse("-c", "test", "-c", "test2", "-c", "test3", "-c", "test4", "-c", "test5");
    }
    
    @Test(expectedExceptions = ParseOptionGroupException.class)
    public void require_one_bad_none() {
        SingleCommand<One> parser = TestingUtil.singleCommandParser(One.class);
        parser.parse();
    }
    
    @Test(expectedExceptions = ParseOptionGroupException.class)
    public void require_one_bad_multiple_01() {
        SingleCommand<One> parser = TestingUtil.singleCommandParser(One.class);
        parser.parse("-a", "test", "-b", "test2");
    }
    
    
    @Test(expectedExceptions = ParseOptionGroupException.class)
    public void require_one_bad_multiple_02() {
        SingleCommand<One> parser = TestingUtil.singleCommandParser(One.class);
        parser.parse("-a", "test", "-c", "test2");
    }
    
    
    @Test(expectedExceptions = ParseOptionGroupException.class)
    public void require_one_bad_multiple_03() {
        SingleCommand<One> parser = TestingUtil.singleCommandParser(One.class);
        parser.parse("-b", "test", "-c", "test2");
    }
}
