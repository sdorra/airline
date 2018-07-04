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

### Format Definitions

#### `PROSE`

For prose format help there should be one/more content blocks.  Each content block is a set of paragraphs i.e. each entry in the `String[]` array returned by `getContentBlock()` will be treated as a paragraph.

#### `LIST`

For list format there should be a one/more content blocks.  Each content block is treated as a list with each string in the content block treated as an item in the list.

#### `TABLE` and `TABLE_WITH_HEADERS`

For table format there should be one/more content blocks where each content block represents a column of the table.  When using the `TABLE_WITH_HEADERS` format the first item in each content block is treated as the header for that column.

#### `EXAMPLES`

There should be 2 or more content blocks.  The first content block contains the examples where each entry is a single example, the subsequent content blocks contain explanatory text corresponding to those examples.  So if you have an example at index 0 in the first content block, index 0 in all subsequent content blocks is taken as explanatory text for that example.

#### `NONE_PRINTABLE`

Standard help generators will ignore this so content blocks can be used however you wish

### Example Implementations

Let's take a look at a couple of implementations from built-in restriction implementations.

Firstly the simple {% include github-ref.md class="NotBlankRestriction" package="restrictions.common" %} implementation:

```java
    @Override
    public String getPreamble() {
        return null;
    }

    @Override
    public HelpFormat getFormat() {
        return HelpFormat.PROSE;
    }

    @Override
    public int numContentBlocks() {
        return 1;
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        if (blockNumber != 0) throw new IndexOutOfBoundsException();
        
        return new String[] { "This options value cannot be blank (empty or all whitespace)" };
    }
```

Which is pretty simple and self explanatory.

Now let's look at a slightly more complex example with list content - {% include github-ref.md class="AbstractAllowedValuesRestriction" package="restrictions.common" %}:

```java
    @Override
    public HelpFormat getFormat() {
        return HelpFormat.LIST;
    }

    @Override
    public int numContentBlocks() {
        return 1;
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        if (blockNumber != 0)
            throw new IndexOutOfBoundsException();
        return this.rawValues.toArray(new String[this.rawValues.size()]);
    }

    public Set<String> getAllowedValues() {
        return this.rawValues;
    }
```

And finally let's look at a complex example with tabular content - {% include github-ref.md class="VersionSection" package="help.sections.common" %}:

```java
@Override
    public String getPreamble() {
        return null;
    }

    @Override
    public HelpFormat getFormat() {
        if (this.versions.size() == 0)
            return HelpFormat.NONE_PRINTABLE;
        return this.tabular ? HelpFormat.TABLE_WITH_HEADERS : HelpFormat.LIST;

    }

    @Override
    public int numContentBlocks() {
        if (this.tabular) {
            return 4 + this.titles.length;
        } else {
            return this.versions.size();
        }
    }

    @Override
    public String[] getContentBlock(int blockNumber) {
        if (blockNumber < 0 || blockNumber > this.numContentBlocks())
            throw new IndexOutOfBoundsException();

        if (this.tabular) {
            String[] column = new String[this.versions.size() + 1];
            for (int row = 0; row < this.versions.size(); row++) {
                switch (blockNumber) {
                case 0:
                    column[0] = "Component";
                    this.versions.get(row).addComponent(column, row + 1);
                    break;
                case 1:
                    column[0] = "Version";
                    this.versions.get(row).addVersion(column, row + 1);
                    break;
                case 2:
                    column[0] = "Build";
                    this.versions.get(row).addBuild(column, row + 1);
                    break;
                case 3:
                    column[0] = "Build Date";
                    this.versions.get(row).addBuildDate(column, row + 1);
                    break;
                default:
                    column[0] = this.titles[blockNumber - 4];
                    this.versions.get(row).addAdditionalColumn(column, row + 1, this.titles[blockNumber - 4]);
                    break;
                }
            }
            return column;
        } else {
            return this.versions.get(blockNumber).toList();
        }
    }
```

Here we have an implementation that dynamically generates its content blocks based on its data and supports both list and tabular format help.