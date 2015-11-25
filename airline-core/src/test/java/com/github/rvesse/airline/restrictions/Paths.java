package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.PathKind;
import com.github.rvesse.airline.annotations.restrictions.Path;

@Command(name = "paths")
public class Paths {

    @Option(name = "--path", arity = 1)
    @Path(mustExist = true)
    public String pathMustExist;
    
    @Option(name = "--file", arity = 1)
    @Path(mustExist = true, kind = PathKind.FILE)
    public String fileMustExit;
    
    @Option(name = "--directory", arity = 1)
    @Path(mustExist = true, kind = PathKind.DIRECTORY)
    public String dirMustExist;
    
    @Option(name = "--readable", arity = 1)
    @Path(mustExist = false, readable = true, writable = false, executable = false)
    public String readable;
    
    @Option(name = "--writable", arity = 1)
    @Path(mustExist = false, writable = true, readable = false, executable = false)
    public String writable;
    
    @Option(name = "--executable", arity = 1)
    @Path(mustExist = false, executable = true, readable = false, writable = false)
    public String executable;
    
    @Option(name = "--any", arity = 1)
    @Path(mustExist = false, readable = false, writable = false, executable = false, kind = PathKind.ANY)
    public String anyPath;
}
