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

package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.Date;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class FssSickReport extends DocflowDescription {

    private FormVersion formVersion;

    private String registrationNumber;

    private String fssId;

    private String fssStageDescription;

    private String fssStageErrorCode;

    private String fssStageErrorExtend;

    private String fssStageType;

    private String fssStageStatus;

    private Date fssStageDate;

    public FormVersion getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(FormVersion formVersion) {
        this.formVersion = formVersion;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFssId() {
        return fssId;
    }

    public void setFssId(String fssId) {
        this.fssId = fssId;
    }

    public String getFssStageDescription() {
        return fssStageDescription;
    }

    public void setFssStageDescription(String fssStageDescription) {
        this.fssStageDescription = fssStageDescription;
    }

    public String getFssStageErrorCode() {
        return fssStageErrorCode;
    }

    public void setFssStageErrorCode(String fssStageErrorCode) {
        this.fssStageErrorCode = fssStageErrorCode;
    }

    public String getFssStageErrorExtend() {
        return fssStageErrorExtend;
    }

    public void setFssStageErrorExtend(String fssStageErrorExtend) {
        this.fssStageErrorExtend = fssStageErrorExtend;
    }

    public String getFssStageType() {
        return fssStageType;
    }

    public void setFssStageType(String fssStageType) {
        this.fssStageType = fssStageType;
    }

    public String getFssStageStatus() {
        return fssStageStatus;
    }

    public void setFssStageStatus(String fssStageStatus) {
        this.fssStageStatus = fssStageStatus;
    }

    public Date getFssStageDate() {
        return fssStageDate;
    }

    public void setFssStageDate(Date fssStageDate) {
        this.fssStageDate = fssStageDate;
    }
}
