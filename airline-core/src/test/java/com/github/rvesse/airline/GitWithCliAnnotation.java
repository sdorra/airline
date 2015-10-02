package com.github.rvesse.airline;

import com.github.rvesse.airline.Git.Add;
import com.github.rvesse.airline.Git.RemoteAdd;
import com.github.rvesse.airline.Git.RemoteShow;
import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Group;
import com.github.rvesse.airline.help.Help;

//@formatter:off
@Cli(name = "git",
     description = "the stupid content tracker", 
     defaultCommand = Help.class, 
     commands = { Help.class, Add.class }, 
     groups = {
        @Group(name = "remote",
               description = "Manage set of tracked repositories",
               defaultCommand = RemoteShow.class,
               commands = { RemoteShow.class, RemoteAdd.class })
     }
)
//@formatter:on
public class GitWithCliAnnotation extends Git {

    public static void main(String[] args) {
        com.github.rvesse.airline.Cli<Runnable> gitParser = new com.github.rvesse.airline.Cli<Runnable>(
                GitWithCliAnnotation.class);

        gitParser.parse(args).run();
    }
}
