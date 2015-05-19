package com.github.rvesse.airline.args;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.OptionType;

@Command(name="GlobalOptionsHidden")
public class GlobalOptionsHidden
{
    @Option(type = OptionType.GLOBAL, name = {"-hd", "--hidden"}, hidden = true)
    public boolean hiddenOption;

    @Option(type = OptionType.GLOBAL, name = {"-op" ,"--optional"}, hidden = false)
    public boolean optionalOption;
}
