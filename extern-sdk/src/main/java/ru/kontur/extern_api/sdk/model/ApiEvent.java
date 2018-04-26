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

package ru.kontur.extern_api.sdk.model;

import java.util.Date;
import java.util.Objects;


/**
 * @author AlexS
 */
public class ApiEvent {

    private String inn = null;
    private String kpp = null;
    private String docflowType = null;
    private Link docflowLink = null;
    private String newState = null;
    private Date eventDateTime = null;
    private String id = null;

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getDocflowType() {
        return docflowType;
    }

    public void setDocflowType(String docflowType) {
        this.docflowType = docflowType;
    }

    public Link getDocflowLink() {
        return docflowLink;
    }

    public void setDocflowLink(Link docflowLink) {
        this.docflowLink = docflowLink;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiEvent apiEvent = (ApiEvent) o;
        return Objects.equals(this.inn, apiEvent.inn)
                && Objects.equals(this.kpp, apiEvent.kpp)
                && Objects.equals(this.docflowType, apiEvent.docflowType)
                && Objects.equals(this.docflowLink, apiEvent.docflowLink)
                && Objects.equals(this.newState, apiEvent.newState)
                && Objects.equals(this.eventDateTime, apiEvent.eventDateTime)
                && Objects.equals(this.id, apiEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn, kpp, docflowType, docflowLink, newState, eventDateTime, id);
    }
}
