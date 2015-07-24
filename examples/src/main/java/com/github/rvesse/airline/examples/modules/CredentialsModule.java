package com.github.rvesse.airline.examples.modules;

import com.github.rvesse.airline.annotations.Option;

public class CredentialsModule {

    @Option(name = "--username", title = "User", arity = 1, description = "Sets the username to use")
    public String user;
    
    @Option(name = "--password", title = "Password", arity = 1, description = "Sets the password to use")
    public String password;
}
