```java
public static void main(String[] args) {
        com.github.rvesse.airline.Cli<ExampleRunnable> cli = new com.github.rvesse.airline.Cli<ExampleRunnable>(ShipItCli.class);
        try {
            // Parse with a result to allow us to inspect the results of parsing
            ParseResult<ExampleRunnable> result = cli.parseWithResult(args);
            if (result.wasSuccessful()) {
                // Parsed successfully, so just run the command and exit
                System.exit(result.getCommand().run());
            } else {
                // Parsing failed
                // Display errors and then the help information
                System.err.println(String.format("%d errors encountered:", result.getErrors().size()));
                int i = 1;
                for (ParseException e : result.getErrors()) {
                    System.err.println(String.format("Error %d: %s", i, e.getMessage()));
                    i++;
                }
                
                System.err.println();
                
                com.github.rvesse.airline.help.Help.<ExampleRunnable>help(cli.getMetadata(), Arrays.asList(args), System.err);
            }
        } catch (Exception e) {
            // Errors should be being collected so if anything is thrown it is unexpected
            System.err.println(String.format("Unexpected error: %s", e.getMessage()));
            e.printStackTrace(System.err);
        }
        
        // If we got here we are exiting abnormally
        System.exit(1);
    }
```