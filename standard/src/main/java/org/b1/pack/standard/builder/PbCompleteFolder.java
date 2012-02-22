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

package org.b1.pack.standard.builder;

import org.b1.pack.api.builder.PbFolder;
import org.b1.pack.standard.common.CompositeWritable;

import java.io.IOException;
import java.io.OutputStream;

public class PbCompleteFolder extends CompositeWritable {

    private final PbFolder folder;

    public PbCompleteFolder(PbRecordHeader header, PbFolder folder) {
        super(header);
        this.folder = folder;
    }

    @Override
    public void writeTo(OutputStream stream, long start, long end) throws IOException {
        if (start == 0 && folder != null) {
            folder.beforeAdd();
        }
        super.writeTo(stream, start, end);
    }
}