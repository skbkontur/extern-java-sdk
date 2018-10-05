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


/**
 * <p>
 * Класс предназначен для хронения информации ленты событий документооборотов см. класс
 * EventService
 * </p>
 */
public class ApiEvent {

    private String inn;
    private String kpp;
    private DocflowType docflowType;
    private Link docflowLink;
    private DocflowStatus newState;
    private Date eventDateTime;
    private String id;

    /**
     * Возвращает ИНН подотчетной организации
     *
     * @return ИНН подотчетной организации
     */
    public String getInn() {
        return inn;
    }

    /**
     * Возвращает КПП подотчетной организации
     *
     * @return КПП подотчетной организации
     */
    public String getKpp() {
        return kpp;
    }

    /**
     * Возвращает тип документооборота
     * <ul>
     * <li>urn:docflow-type:ke-fns-report</li>
     * </ul>
     *
     * @return тип документооборота
     */
    public DocflowType getDocflowType() {
        return docflowType;
    }

    /**
     * Возвращает ссылку на документооборот
     *
     * @return ссылка на документооборот
     */
    public Link getDocflowLink() {
        return docflowLink;
    }

    /**
     * Возвращает состояние документооборота:
     * <ul>
     * <li>urn:transaction:report</li>
     * <li>urn:transaction:acceptance-result-positive</li>
     * <li>urn:transaction:processing-result-ok</li>
     * </ul>
     *
     * @return состояние документооборота
     */
    public DocflowStatus getNewState() {
        return newState;
    }

    /**
     * Возвращает дату события документооборота
     *
     * @return дата события
     */
    public Date getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Возвращает идентификатор события
     *
     * @return идентификатор события
     */
    public String getId() {
        return id;
    }


}
