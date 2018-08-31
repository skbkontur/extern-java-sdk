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

public class StatReport extends DocflowDescription {

    private FormVersion formVersion;

    private String recipient;

    private String okpo;

    private String okud;

    private Date periodBegin;

    private Date periodEnd;

    private int periodCode;

    private int correctionNumber;

    public FormVersion getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(FormVersion formVersion) {
        this.formVersion = formVersion;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getOkud() {
        return okud;
    }

    public void setOkud(String okud) {
        this.okud = okud;
    }

    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public int getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(int periodCode) {
        this.periodCode = periodCode;
    }

    public int getCorrectionNumber() {
        return correctionNumber;
    }

    public void setCorrectionNumber(int correctionNumber) {
        this.correctionNumber = correctionNumber;
    }
}
