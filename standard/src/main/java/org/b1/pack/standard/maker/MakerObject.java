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

package org.b1.pack.standard.maker;

import com.google.common.base.Charsets;
import org.b1.pack.api.maker.PmObject;
import org.b1.pack.standard.common.Numbers;
import org.b1.pack.standard.common.ObjectKey;
import org.b1.pack.standard.common.RecordPointer;

import java.io.IOException;
import java.io.OutputStream;

public abstract class MakerObject {

    private final long id;
    private final Long modifiedAt;
    private RecordPointer pointer;

    protected MakerObject(long id, PmObject object) {
        this.id = id;
        this.modifiedAt = object == null ? System.currentTimeMillis() : object.getLastModifiedTime();
    }

    public long getId() {
        return id;
    }

    public abstract void writeCatalogRecord(ObjectKey key, OutputStream stream) throws IOException;

    protected void writeCompleteRecordPart(int recordType, ObjectKey key, PackRecordStream recordStream) throws IOException {
        pointer = recordStream.getCurrentPointer();
        Numbers.writeLong(recordType, recordStream);
        writeHeader(key, recordStream);
    }

    protected void writeCatalogRecordPart(int recordType, ObjectKey key, OutputStream stream) throws IOException {
        Numbers.writeLong(recordType, stream);
        writePointer(stream);
        writeHeader(key, stream);
    }

    private void writePointer(OutputStream stream) throws IOException {
        Numbers.writeLong(pointer.volumeNumber, stream);
        Numbers.writeLong(pointer.blockOffset, stream);
        Numbers.writeLong(pointer.recordOffset, stream);
    }

    private void writeHeader(ObjectKey key, OutputStream stream) throws IOException {
        Numbers.writeLong(id, stream);
        Numbers.writeLong(key.getParentId(), stream);
        byte[] name = key.getName().getBytes(Charsets.UTF_8);
        Numbers.writeLong(name.length, stream);
        stream.write(name);
        if (modifiedAt != null) {
            Numbers.writeLong(0, stream);
            Numbers.writeLong(modifiedAt, stream);
        }
        Numbers.writeLong(null, stream);
    }
}