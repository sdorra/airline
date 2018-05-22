package com.github.rvesse.airline.parser.aliases.locators;

public abstract class AbstractPlaceholderLocator extends FileLocator {

    private static final String PLACEHOLDER_START = "${";
    private static final String PLACEHOLDER_END = "}";

    @Override
    protected String resolve(String searchLocation) {
        if (!searchLocation.contains(PLACEHOLDER_START) && !searchLocation.contains(PLACEHOLDER_END)) {
            return searchLocation;
        }

        // Resolve all placeholders
        StringBuilder output = new StringBuilder();
        char[] cs = searchLocation.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];

            switch (c) {
            case '$':
                // Possible start of placeholder
                if (i < cs.length - 1 && cs[i + 1] == '{') {
                    // Start of a placeholder
                    // Find the end
                    int j;
                    for (j = i + 1; j < cs.length; j++) {
                        if (cs[j] == '}')
                            break;
                    }
                    if (j == cs.length && cs[j - 1] != '}') {
                        // Invalid placeholder as no terminating }
                        // Just append the remaining string and stop
                        output.append(cs, i, cs.length - i);
                        i = j;
                        break;
                    } else {
                        // Valid placeholder
                        String placeholder = new String(cs, i + 2, j - i - 2);
                        output.append(resolvePlaceholder(placeholder));
                        i = j;
                        continue;
                    }
                }
                // Intentionally dropping through if not a placeholder start
            default:
                // Regular character
                output.append(c);
            }
        }

        return output.toString();
    }

    /**
     * Resolves a placeholder
     * 
     * @param name
     *            Placeholder name
     * @return Resolved placeholder
     */
    protected abstract String resolvePlaceholder(String name);
}
