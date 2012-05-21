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
import com.google.common.collect.ImmutableMap;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.b1.pack.api.common.PackFormat.B1;

public class ArgSet {

    public static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)([kMG]?B)");
    public static final Pattern TYPE_PATTERN = Pattern.compile("(.*?)(?::(.+))?");
    public static final ImmutableMap<String, Integer> SIZE_MULTIPLIERS = ImmutableMap.<String, Integer>builder()
            .put("B", 1).put("kB", 1000).put("MB", 1000 * 1000).put("GB", 1000 * 1000 * 1000)
            .put("KiB", 1024).put("MiB", 1024 * 1024).put("GiB", 1024 * 1024 * 1024).build();

    private String command;
    private String packName;
    private List<String> fileNames;
    private boolean help;
    private String outputDirectory;
    private Long maxVolumeSize;
    private String compression;
    private String encryption;
    private String password;
    private String typeFormat = B1;
    private String typeFlag;

    public ArgSet(String[] args) {
        OptionParser parser = new OptionParser();
        OptionSpec helpOption = parser.acceptsAll(Arrays.asList("?", "h", "help"));
        OptionSpec<String> outputOption = parser.accepts("o").withRequiredArg();
        OptionSpec<String> volumeOption = parser.accepts("v").withRequiredArg();
        OptionSpec<String> methodOption = parser.accepts("m").withRequiredArg();
        OptionSpec<String> encryptOption = parser.accepts("encrypt").withRequiredArg();
        OptionSpec<String> passwordOption = parser.accepts("password").withRequiredArg();
        OptionSpec<String> typeOption = parser.accepts("type").withRequiredArg();
        OptionSet optionSet = parser.parse(args);
        LinkedList<String> arguments = new LinkedList<String>(optionSet.nonOptionArguments());
        command = arguments.pollFirst();
        packName = arguments.pollFirst();
        fileNames = arguments;
        help = optionSet.has(helpOption);
        outputDirectory = optionSet.valueOf(outputOption);
        initMaxVolumeSize(optionSet.valueOf(volumeOption));
        compression = optionSet.valueOf(methodOption);
        encryption = optionSet.valueOf(encryptOption);
        password = optionSet.valueOf(passwordOption);
        initType(optionSet.valueOf(typeOption));
    }

    private void initMaxVolumeSize(String size) {
        if (size == null) return;
        Matcher matcher = SIZE_PATTERN.matcher(size);
        Preconditions.checkArgument(matcher.matches(), "Invalid volume size: %s", size);
        maxVolumeSize = Long.parseLong(matcher.group(1)) * SIZE_MULTIPLIERS.get(matcher.group(2));
    }

    private void initType(String type) {
        if (type == null) return;
        Matcher matcher = TYPE_PATTERN.matcher(type);
        Preconditions.checkArgument(matcher.matches());
        typeFormat = matcher.group(1);
        typeFlag = matcher.group(2);
    }

    public String getCommand() {
        return command;
    }

    public String getPackName() {
        return packName;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public boolean isHelp() {
        return help;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public Long getMaxVolumeSize() {
        return maxVolumeSize;
    }

    public String getCompression() {
        return compression;
    }

    public String getEncryption() {
        return encryption;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeFormat() {
        return typeFormat;
    }

    public String getTypeFlag() {
        return typeFlag;
    }
}
