/**
 * Copyright (C) 2010-15 the original author or authors.
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
package com.github.rvesse.airline.restrictions.common;

import java.io.File;

import com.github.rvesse.airline.annotations.restrictions.FileKind;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

public class FileRestriction extends AbstractCommonRestriction {

    private final boolean mustExist, readable, writeable, executable;
    private final FileKind kind;

    public FileRestriction(boolean mustExist, boolean readable, boolean writeable, boolean executable, FileKind kind) {
        this.mustExist = mustExist;
        this.readable = readable;
        this.writeable = writeable;
        this.executable = executable;
        this.kind = kind;
    }

    protected void validate(String title, String path) {
        try {
            File f = new File(path);

            if (this.mustExist && !f.exists())
                throw new ParseRestrictionViolatedException(
                        "%s was given value '%s' which is not a path to an existing file/directory", title, path);

            if (this.mustExist && f.exists()) {
                // Some things require the file to exist in order for validation
                // to be applied
                switch (kind) {
                case FILE:
                    if (!f.isFile())
                        throw new ParseRestrictionViolatedException(
                                "%s was given value '%s' which is not a path to a file", title, path);
                    break;
                case DIRECTORY:
                    if (!f.isDirectory())
                        throw new ParseRestrictionViolatedException(
                                "%s was given value '%s' which is not a path to a directory", title, path);
                    break;
                default:
                    if (!f.isFile() && !f.isDirectory())
                        throw new ParseRestrictionViolatedException(
                                "%s was given value '%s' which is not a path to a file/directory", title, path);
                    break;
                }

                if (this.readable && !f.canRead())
                    throw new ParseRestrictionViolatedException("%s was given value '%s' which it not a readable path",
                            title, path);

                if (this.writeable && !f.canWrite())
                    throw new ParseRestrictionViolatedException("%s was given value '%s' which is not a writeable path",
                            title, path);

                if (this.executable && !f.canExecute())
                    throw new ParseRestrictionViolatedException(
                            "%s was given value '%s' which is not a executable path", title, path);
            } else if (this.writeable) {
                // Verify that the portion of the path that exists is write-able
                if (!f.isAbsolute())
                    f = f.getAbsoluteFile();
                while (f.getParentFile() != null) {
                    f = f.getParentFile();
                    if (f.exists()) {
                        if (!f.canWrite())
                            throw new ParseRestrictionViolatedException(
                                    "%s was given value '%s' which is not a writeable path", title, path);

                        // If we reach here a portion of the path is write-able
                        // so in principal the whole path is write-able
                        return;
                    }
                }
            }

        } catch (NullPointerException e) {
            throw new ParseRestrictionViolatedException("%s must be a non-null path", title);
        }
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        this.validate(String.format("Option '%s'", option.getTitle()), value);
    }

    @Override
    public <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        this.validate(String.format("Argument '%s'", AirlineUtils.first(arguments.getTitle())), value);
    }

}
