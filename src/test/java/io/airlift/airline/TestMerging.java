/**
 * Copyright (C) 2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.airlift.airline;

import io.airlift.airline.args.ArgsMergeAddition;
import io.airlift.airline.args.ArgsMergeOverride;
import io.airlift.airline.args.ArgsMergeSealed;
import io.airlift.airline.args.ArgsMergeSealedOverride;
import io.airlift.airline.args.ArgsMergeUndeclaredOverride;
import io.airlift.airline.model.ArgumentsMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import org.testng.annotations.Test;

import static io.airlift.airline.TestingUtil.singleCommandParser;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

/**
 * Tests behaviours with regards to merging options from class hierarchies
 * 
 */
public class TestMerging {

    /**
     * Finds an option by name
     * 
     * @param metadata
     *            Metadata
     * @param name
     *            Name
     * @return Option if found, null otherwise
     */
    private OptionMetadata findByName(CommandMetadata metadata, String name) {
        for (OptionMetadata opData : metadata.getAllOptions()) {
            if (opData.getOptions().contains(name))
                return opData;
        }
        return null;
    }

    @Test
    public void merging_additive() {
        Cli<ArgsMergeAddition> parser = singleCommandParser(ArgsMergeAddition.class);
        CommandMetadata metadata = parser.getMetadata().getDefaultGroupCommands().get(0);

        OptionMetadata verboseOption = findByName(metadata, "-v");
        assertNotNull(verboseOption);
        assertFalse(verboseOption.isHidden());
        
        OptionMetadata hiddenOption = findByName(metadata, "--hidden");
        assertNotNull(hiddenOption);
        assertTrue(hiddenOption.isHidden());
        
        ArgumentsMetadata argsData = metadata.getArguments();
        assertNotNull(argsData);
    }
    
    @Test
    public void merging_declared_override() {
        Cli<ArgsMergeOverride> parser = singleCommandParser(ArgsMergeOverride.class);
        CommandMetadata metadata = parser.getMetadata().getDefaultGroupCommands().get(0);

        OptionMetadata verboseOption = findByName(metadata, "-v");
        assertNotNull(verboseOption);
        assertFalse(verboseOption.isHidden());
        
        OptionMetadata hiddenOption = findByName(metadata, "--hidden");
        assertNotNull(hiddenOption);
        assertTrue(hiddenOption.isOverride());
        assertFalse(hiddenOption.isHidden());
        assertFalse(hiddenOption.isSealed());
        
        ArgumentsMetadata argsData = metadata.getArguments();
        assertNotNull(argsData);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void merging_undeclared_override() {
        Cli<ArgsMergeUndeclaredOverride> parser = singleCommandParser(ArgsMergeUndeclaredOverride.class);
        parser.getMetadata().getDefaultGroupCommands().get(0);
    }
    
    @Test
    public void merging_sealed() {
        Cli<ArgsMergeSealed> parser = singleCommandParser(ArgsMergeSealed.class);
        CommandMetadata metadata = parser.getMetadata().getDefaultGroupCommands().get(0);

        OptionMetadata verboseOption = findByName(metadata, "-v");
        assertNotNull(verboseOption);
        assertFalse(verboseOption.isHidden());
        
        OptionMetadata hiddenOption = findByName(metadata, "--hidden");
        assertNotNull(hiddenOption);
        assertTrue(hiddenOption.isOverride());
        assertFalse(hiddenOption.isHidden());
        assertTrue(hiddenOption.isSealed());
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void merging_sealed_override() {
        Cli<ArgsMergeSealedOverride> parser = singleCommandParser(ArgsMergeSealedOverride.class);
        parser.getMetadata().getDefaultGroupCommands().get(0);
    }
}
