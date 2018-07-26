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

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.Objects;


/**
 * <p>
 * Класс предназначен для хронения информации ленты событий документооборотов см. класс EventService
 * </p>
 * @author Aleksey Sukhorukov
 */
public class ApiEvent {

    private String inn = null;
    private String kpp = null;
    @SerializedName("docflow-type")
    private String docflowType = null;
    @SerializedName("docflow-link")
    private Link docflowLink = null;
    @SerializedName("new-state")
    private String newState = null;
    @SerializedName("event-date-time")
    private Date eventDateTime = null;
    private String id = null;

    /**
     * Возвращает ИНН подотчетной организации
     * @return ИНН подотчетной организации
     */
    public String getInn() {
        return inn;
    }

    /**
     * Устанавливает ИНН подотчетной организации
     * @param inn ИНН подотчетной организации
     */
    public void setInn(String inn) {
        this.inn = inn;
    }

    /**
     * Возвращает КПП подотчетной организации
     * @return КПП подотчетной организации
     */
    public String getKpp() {
        return kpp;
    }

    /**
     * Устанавливает КПП подотчетной организации
     * @param kpp КПП подотчетной организации
     */
    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    /**
     * Возвращает тип документооборота
     * <ul>
     *   <li>urn:docflow-type:ke-fns-report</li>
     * </ul>
     * @return тип документооборота
     */
    public String getDocflowType() {
        return docflowType;
    }

    /**
     * Устанавливает тип документооборота
     * @param docflowType тип документооборота:
     * <ul>
     *   <li>urn:docflow-type:ke-fns-report</li>
     * </ul>
     */
    public void setDocflowType(String docflowType) {
        this.docflowType = docflowType;
    }

    /**
     * Возвращает ссылку на документооборот
     * @return ссылка на документооборот
     */
    public Link getDocflowLink() {
        return docflowLink;
    }

    /**
     * Устанавливает ссылку на документооборот
     * @param docflowLink ссылка на документооборот
     */
    public void setDocflowLink(Link docflowLink) {
        this.docflowLink = docflowLink;
    }

    /**
     * Возвращает состояние документооборота:
     * <ul>
     *   <li>urn:transaction:report</li>
     *   <li>urn:transaction:acceptance-result-positive</li>
     *   <li>urn:transaction:processing-result-ok</li>
     * </ul>
     * @return состояние документооборота
     */
    public String getNewState() {
        return newState;
    }

    /**
     * Устанавливает состояние документооборота:
     * <ul>
     *   <li>urn:transaction:report</li>
     *   <li>urn:transaction:acceptance-result-positive</li>
     *   <li>urn:transaction:processing-result-ok</li>
     * </ul>
     * @param newState состояние документооборота
     */
    public void setNewState(String newState) {
        this.newState = newState;
    }

    /**
     * Возвращает дату события документооборота
     * @return дата события
     */
    public Date getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Устанавливает дату события документооборота
     * @param eventDateTime дата события документооборота
     */
    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    /**
     * Возвращает идентификатор события
     * @return идентификатор события
     */
    public String getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор события
     * @param id идентификатор события
     */
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
