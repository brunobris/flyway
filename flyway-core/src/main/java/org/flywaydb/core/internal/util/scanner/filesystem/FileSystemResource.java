/*
 * Copyright 2010-2018 Boxfuse GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.util.scanner.filesystem;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.internal.util.BomStrippingReader;
import org.flywaydb.core.internal.util.FileCopyUtils;
import org.flywaydb.core.internal.util.line.DefaultLineReader;
import org.flywaydb.core.internal.util.line.LineReader;
import org.flywaydb.core.internal.util.scanner.AbstractLoadableResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * A resource on the filesystem.
 */
public class FileSystemResource extends AbstractLoadableResource implements Comparable<FileSystemResource> {
    /**
     * The location of the resource on the filesystem.
     */
    private final File file;
    private final Charset encoding;

    /**
     * Creates a new ClassPathResource.
     *
     * @param location The location of the resource on the filesystem.
     */
    public FileSystemResource(String location, Charset encoding) {
        this.file = new File(new File(location).getPath());
        this.encoding = encoding;
    }

    /**
     * @return The location of the resource on the filesystem.
     */
    @Override
    public String getLocation() {
        return file.getPath();
    }

    /**
     * Retrieves the location of this resource on disk.
     *
     * @return The location of this resource on disk.
     */
    @Override
    public String getLocationOnDisk() {
        return file.getAbsolutePath();
    }

    /**
     * Loads this resource as a string.
     *
     * @return The string contents of the resource.
     */
    @Override
    public LineReader loadAsString() {
        try {





            return new DefaultLineReader(new BomStrippingReader(new InputStreamReader(new FileInputStream(file), encoding)));
        } catch (IOException e) {
            throw new FlywayException("Unable to load filesystem resource: " + file.getPath() + " (encoding: " + encoding + ")", e);
        }
    }

    /**
     * Loads this resource as a byte array.
     *
     * @return The contents of the resource.
     */
    @Override
    public byte[] loadAsBytes() {
        try {
            InputStream inputStream = new FileInputStream(file);
            return FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            throw new FlywayException("Unable to load filesystem resource: " + file.getPath(), e);
        }
    }

    /**
     * @return The filename of this resource, without the path.
     */
    @Override
    public String getFilename() {
        return file.getName();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(FileSystemResource o) {
        return file.compareTo(o.file);
    }
}