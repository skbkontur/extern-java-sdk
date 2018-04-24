/*
 * MIT License
 *
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
 */

package ru.skbkontur.sdk.extern.model;

/**
 * @author AlexS
 */
public class DocumentDescription {

    private String type = null;
    private String filename = null;
    private String contentType = null;

    public DocumentDescription type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DocumentDescription filename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * Get filename
     *
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        // пытаемся угадать тип
        this.type = Type.guessType(filename).value;
    }

    public DocumentDescription contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Get contentType
     *
     * @return contentType
     */
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private enum Type {
        DEKL("urn:document:fns534-report", "^(?i)NO_(\\w+)_(\\d{4})_(\\d{4})_(\\d{12,19})_(\\d{8})_([\\w-]{36})\\.xml$"),
        DOV("urn:document:fns534-report-warrant", "^(?i)ON_DOV_(\\d{4})_(\\d{4})_(\\d{12,19})_(\\d{8})_([\\w-]{36})\\.xml$"),
        APP("urn:document:fns534-report-attachment", ""),
        UNKNOWN(null, "");

        private final String value;
        private final String pattern;

        Type(String value, String pattern) {
            this.value = value;
            this.pattern = pattern;
        }

        private static Type guessType(String fileName) {
            if (fileName == null) return UNKNOWN;

            if (fileName.matches(DOV.pattern))
                return DOV;
            else if (fileName.matches(DEKL.pattern))
                return DEKL;
            else
                return APP;
        }
    }
}
