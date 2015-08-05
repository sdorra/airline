package com.github.rvesse.airline.io.printers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTroffPrinter {

    private static final String BULLET = ".IP \"\\(bu\" 4";
    
    @Test
    public void title_01() {
        StringWriter strWriter = new StringWriter();
        TroffPrinter printer = new TroffPrinter(new PrintWriter(strWriter));
        
        printer.start("Test", 1);
        printer.finish();
        
        //@formatter:off
        String expected = StringUtils.join(new String[] { 
                ".",
                ".TH \"Test\" \"1\" \"\" \"\" \"\"",
                "" 
            }, '\n');
        //@formatter:on
        Assert.assertEquals(strWriter.toString(), expected);
    }
    
    @Test
    public void title_02() {
        StringWriter strWriter = new StringWriter();
        TroffPrinter printer = new TroffPrinter(new PrintWriter(strWriter));
        
        printer.start("Test\"Quotes", 1);
        printer.finish();
        
        //@formatter:off
        String expected = StringUtils.join(new String[] { 
                ".",
                ".TH \"Test Quotes\" \"1\" \"\" \"\" \"\"",
                "" 
            }, '\n');
        //@formatter:on
        Assert.assertEquals(strWriter.toString(), expected);
    }

    @Test
    public void basic_text() {
        StringWriter strWriter = new StringWriter();
        TroffPrinter printer = new TroffPrinter(new PrintWriter(strWriter));
        
        printer.println("This is a line");
        printer.println("Another line");
        printer.finish();
        
        //@formatter:off
        String expected = StringUtils.join(new String[] { 
                ".",
                "This is a line", 
                ".", 
                "Another line", 
                "" 
            }, '\n');
        //@formatter:on
        Assert.assertEquals(strWriter.toString(), expected);
        
    }
    
    @Test
    public void basic_list() {
        StringWriter strWriter = new StringWriter();
        TroffPrinter printer = new TroffPrinter(new PrintWriter(strWriter));
        
        printer.startList();
        printer.println("A");
        printer.nextListItem();
        printer.println("B");
        printer.nextListItem();
        printer.println("C");
        printer.endList();
        printer.finish();
        
        //@formatter:off
        String expected = StringUtils.join(new String[] { 
                BULLET,
                "A", 
                BULLET, 
                "B", 
                BULLET,
                "C",
                ""
            }, '\n');
        //@formatter:on
        Assert.assertEquals(strWriter.toString(), expected);
    }
    
    @Test
    public void nested_list_01() {
        StringWriter strWriter = new StringWriter();
        TroffPrinter printer = new TroffPrinter(new PrintWriter(strWriter));
        
        printer.startList();
        printer.println("A");
        printer.startList();
        printer.println("B");
        printer.endList();
        printer.nextListItem();
        printer.println("C");
        printer.endList();
        printer.finish();
        
        //@formatter:off
        // - A
        //   - B
        // - C
        String expected = StringUtils.join(new String[] { 
                BULLET,
                "A", 
                ".RS",
                BULLET, 
                "B", 
                ".RE",
                BULLET,
                "C",
                ""
            }, '\n');
        //@formatter:on
        Assert.assertEquals(strWriter.toString(), expected);
    }
    
    @Test
    public void nested_list_02() {
        StringWriter strWriter = new StringWriter();
        TroffPrinter printer = new TroffPrinter(new PrintWriter(strWriter));
        
        printer.startList();
        printer.println("A");
        printer.startList();
        printer.println("B");
        printer.endList();
        printer.println("C");
        printer.endList();
        printer.finish();
        
        //@formatter:off
        // - A
        //   - B
        // C
        String expected = StringUtils.join(new String[] { 
                BULLET,
                "A", 
                ".RS",
                BULLET, 
                "B", 
                ".RE",
                ".IP",
                "C",
                ""
            }, '\n');
        //@formatter:on
        Assert.assertEquals(strWriter.toString(), expected);
    }
}
