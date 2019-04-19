/*
 * Copyright (c) 2019 SKB Kontur
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

package ru.kontur.extern_api.sdk.utils;

public class DcDemandData {
    private DcDocument document;
    private DcDocument description;
    private DcDocument[] applications;

    public DcDemandData(){

    }

    public DcDemandData(DcDocument application, DcDocument description, DcDocument[] includedFiles){

        this.document = application;
        this.description = description;
        this.applications = includedFiles;
    }

    public DcDocument getDocument() {
        return document;
    }

    public void setDocument(DcDocument document) {
        this.document = document;
    }

    public DcDocument getDescription() {
        return description;
    }

    public void setDescription(DcDocument description) {
        this.description = description;
    }

    public DcDocument[] getApplications() {
        return applications;
    }

    public void setApplications(DcDocument[] applications) {
        this.applications = applications;
    }
}
