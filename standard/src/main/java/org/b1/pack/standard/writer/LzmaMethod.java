/*
 * Copyright 2012 b1.org
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

package org.b1.pack.standard.writer;

import org.b1.pack.api.common.PackEntry;
import org.b1.pack.api.compression.CompressionMethod;

class LzmaMethod {

    private static final String[] COMPRESSED_FORMATS = {"jpg", "png", "mp3", "aac", "flac", "wma", "m4a", "avi", "mkv", "mp4", "m4v"};

    private final String name;
    private final long solidBlockSize;
    private final int dictionarySize;
    private final int numberOfFastBytes;
    private final boolean smart;

    public LzmaMethod(String name, long solidBlockSize, int dictionarySize, int numberOfFastBytes, boolean smart) {
        this.name = name;
        this.solidBlockSize = solidBlockSize;
        this.dictionarySize = dictionarySize;
        this.numberOfFastBytes = numberOfFastBytes;
        this.smart = smart;
    }

    public String getName() {
        return name;
    }

    public long getSolidBlockSize() {
        return solidBlockSize;
    }

    public int getDictionarySize() {
        return dictionarySize;
    }

    public int getNumberOfFastBytes() {
        return numberOfFastBytes;
    }

    public boolean isCompressible(PackEntry entry) {
        if (smart) {
            String name = entry.getName();
            int dotIndex = name.lastIndexOf('.');
            String extension = dotIndex < 0 ? "" : name.substring(dotIndex + 1).toLowerCase();
            for (String compressedFormat : COMPRESSED_FORMATS) {
                if (compressedFormat.equals(extension)) return false;
            }
        }
        return true;
    }

    public static LzmaMethod valueOf(CompressionMethod method) {
        if (method == null) {
            return null;
        }
        String name = method.getName();
        if (name.equals(CompressionMethod.SMART.getName()) || name.equals(CompressionMethod.CLASSIC.getName())) {
            return new LzmaMethod(name, 1 << 27, 1 << 20, 1 << 5, name.equals(CompressionMethod.SMART.getName()));
        }
        if (name.equals(CompressionMethod.MAXIMUM.getName())) {
            return new LzmaMethod(name, 1L << 32, 1 << 25, 1 << 6, false);
        }
        throw new IllegalArgumentException("Unknown compression method: " + name);
    }
}
