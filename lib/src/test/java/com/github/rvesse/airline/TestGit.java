package com.github.rvesse.airline;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

public class TestGit
{
    @Test
    public void test()
    {
        // simple command parsing example
        git("add", "-p", "file");
        git("remote", "add", "origin", "git@github.com:airlift/airline.git");
        git("-v", "remote", "show", "origin");
        // test default command
        git("remote");
        git("remote", "origin");
        git("remote", "-n", "origin");
        git("-v", "remote", "origin");

        // show help
        git();
        git("help");
        git("help", "git");
        git("help", "add");
        git("help", "remote");
        git("help", "remote", "show");
    }
    
    private void git(String... args)
    {
        System.out.println("$ git " + StringUtils.join(args, ' '));
        Git.main(args);
        System.out.println();
    }
}
