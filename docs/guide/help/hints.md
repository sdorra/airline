---
layout: page
title: Help Hints
---

Help Hints are a basic component of the help system used by both [Restrictions](../restrictions/index.html) and [Help sections](sections.html). The help hint interface looks like the following:

```java
public interface HelpHint {

    /**
     * Gets the preamble text that should be included
     * 
     * @return Preamble text
     */
    public String getPreamble();

    /**
     * Gets the format of the provided help information
     * 
     * @return Help format
     */
    public HelpFormat getFormat();

    /**
     * Gets the number of content blocks provided
     * <p>
     * Help generators should consult the {@link #getFormat()} return value to
     * determine how to format the content blocks but they are not required to
     * do so
     * </p>
     * 
     * @return Number of content blocks
     */
    public int numContentBlocks();

    /**
     * Gets the content block with the given number
     * 
     * @param blockNumber
     *            Block number
     * @return Content Block
     */
    public String[] getContentBlock(int blockNumber);
}
```

Firstly `getPreamble()` provides an optional preamble text that help generators should include prior to the hint content.

Secondly the `getFormat()` method indicates the type of help content provided. The following formats are supported:

| Help Format | Description |
| --- | --- |
| `PROSE` | Paragraphs of text |
| `LIST` | List of items |
| `TABLE` | Table without headers |
| `TABLE_WITH_HEADERS` | Table with header row |
| `EXAMPLES` | Set of examples plus explanatory text |
| `NONE_PRINTABLE` | Non-printable content, extension point for carrying extra information for custom help generators |

The `numContentBlocks()` method returns the number of content blocks provided and the corresponding `getContentBlock()` method retrieves content blocks. The exact format of data that should be returned from this method depends on the declared `HelpFormat`.

### `PROSE`

For prose format help there should be one/more