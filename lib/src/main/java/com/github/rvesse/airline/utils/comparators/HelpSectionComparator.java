package com.github.rvesse.airline.utils.comparators;

import java.util.Comparator;

import com.github.rvesse.airline.help.sections.HelpSection;

public class HelpSectionComparator implements Comparator<HelpSection> {

    @Override
    public int compare(HelpSection o1, HelpSection o2) {
        if (o1 == null) {
            if (o2 == null)
                return 0;
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        int comparison = Integer.compare(o1.suggestedOrder(), o2.suggestedOrder());
        if (comparison == 0) {
            if (o1.getTitle() == null) {
                if (o2.getTitle() != null) {
                    comparison = -1;
                }
            } else if (o2.getTitle() == null) {
                comparison = 1;
            } else {
                comparison = o1.getTitle().compareTo(o2.getTitle());
            }
        }
        return comparison;
    }

}
