/*
 * Copyright 2011 b1.org
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

package org.b1.pack.cli;

import com.google.common.base.Preconditions;
import org.b1.pack.api.reader.ReaderProvider;

import java.io.Console;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FsReaderProvider extends ReaderProvider {

    private static final Pattern PATTERN = Pattern.compile("(?i)(.*\\.part)(\\d+)(.b1)");

    private final String password;

    protected FsReaderProvider(String password) {
        this.password = password;
    }

    public static ReaderProvider getInstance(String password, File packFile) {
        Matcher matcher = PATTERN.matcher(packFile.getPath());
        if (!matcher.matches()) {
            return new BasicFsReaderProvider(password, packFile);
        }
        String prefix = matcher.group(1);
        String number = matcher.group(2);
        String suffix = matcher.group(3);
        return new MultipartFsReaderProvider(password, prefix, suffix, number.length(), Integer.parseInt(number));
    }

    @Override
    public char[] getPassword() {
        if (password != null) {
            return password.toCharArray();
        }
        Console console = Preconditions.checkNotNull(System.console(), "Console is not available for password input");
        return console.readPassword("Enter password for decryption (will not be echoed): ");
    }
}