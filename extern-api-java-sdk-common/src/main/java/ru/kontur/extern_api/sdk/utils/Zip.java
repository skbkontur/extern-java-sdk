/*
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class Zip {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Unzip single file from zip archive
     *
     * @param compressed single zipped file
     * @return unzipped file bytes
     */
    public static byte[] unzip(byte[] compressed) {
        if (compressed == null) {
            return null;
        }

        byte[] buffer = new byte[BUFFER_SIZE];

        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(compressed);
                ZipArchiveInputStream compressInput = new ZipArchiveInputStream(byteIn);
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {

            int len;

            compressInput.getNextEntry();
            while ((len = compressInput.read(buffer)) > 0) {
                byteOut.write(buffer, 0, len);
            }

            return byteOut.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
