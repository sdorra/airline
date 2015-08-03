package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.MaxLength;
import com.github.rvesse.airline.annotations.restrictions.MinLength;
import com.github.rvesse.airline.annotations.restrictions.NotBlank;
import com.github.rvesse.airline.annotations.restrictions.NotEmpty;
import com.github.rvesse.airline.annotations.restrictions.Pattern;

@Command(name = "strings")
public class Strings {

    @Option(name = "--not-empty")
    @NotEmpty
    public String notEmpty;
    
    @Option(name = "--not-blank")
    @NotBlank
    public String notBlank;
    
    @Option(name = "--min")
    @MinLength(length = 4)
    public String minLength;
    
    @Option(name = "--max")
    @MaxLength(length = 4)
    public String maxLength;
    
    @Option(name = "--tel")
    @Pattern(pattern = "(\\+1-)?\\d{3}-\\d{3}-\\d{4}")
    public String tel;
    
}
