package com.github.rvesse.airline;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

public class TestPing
{
    @Test
    public void test()
    {
        // simple command parsing example
        ping();
        ping("-c", "5");
        ping("--count", "9");
        ping("--count=8");

        // show help
        ping("-h");
        ping("--help");
    }

    private void ping(String... args)
    {
        System.out.println("$ ping " + StringUtils.join(args, ' '));
        Ping.main(args);
        System.out.println();
    }
}
