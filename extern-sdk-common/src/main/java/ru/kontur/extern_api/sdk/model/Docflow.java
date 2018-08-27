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
import java.util.List;
import java.util.UUID;


/**
 * <p>
 * Класс предоставляет информацию о документообороте (ДО).
 * Используется в сервисах: {@code DraftService} и {@code DocflowService}.
 * </p>
 * @author Aleksey Sukhorukov
 */
public class Docflow {

    private UUID id = null;
    private DocflowType type = null;
    private DocflowStatus status = null;
    private IDocflowDescription description = null;
    private List<Document> documents = null;
    private List<Link> links = null;
    private Date sendDate = null;
    private Date lastChangeDate = null;

    /**
     * Возвращает идентификатор ДО
     * @return id идентификатор ДО
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор ДО
     * @param id идентификатор ДО
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает тип ДО:
     * <ul>
     *   <li>urn:docflow:fns534-report</li>
     * </ul>
     * @return type тип ДО
     */
    public DocflowType getType() {
        return type;
    }

    /**
     * Устанавливает тип ДО
     * @param type тип ДО:
     * <ul>
     * <li>urn:docflow-type:ke-fns-report</li>
     * </ul>
     */
    public void setType(DocflowType type) {
        this.type = type;
    }

    /**
     * Возвращает состояние документооборота:
     * <ul>
     *   <li>urn:docflow-common-status:sent - документы были отправлены в контролирующий орган (КО)</li>
     *   <li>urn:docflow-common-status:delivered - от КО пришел документ, подтверждающий доставку документов до сотрудника КО</li>
     *   <li>urn:docflow-common-status:response-arrived - пришли результаты проверки отправленного документа</li>
     *   <li>urn:docflow-common-status:response-processed - пришли результаты обработки отправленного документа</li>
     *   <li>urn:docflow-common-status:received - документ получен КО</li>
     *   <li>urn:docflow-common-status:arrived - документ получен налогоплательщиком</li>
     *   <li>urn:docflow-common-status:processed - налогоплательщик отправил результат приема документа КО</li>
     *   <li>urn:docflow-common-status:finished - завершен полный цикл ДО, в нем есть все транзакции, предусмотренные регламентом</li>
     * </ul>
     * @return состояние документооборота
     */
    public DocflowStatus getStatus() {
        return status;
    }

    /**
     * Устанавливает состояние документооборота
     * @param status состояние документооборота:
     * <ul>
     *  <li>urn:docflow-common-status:sent - документы были отправлены в контролирующий орган (КО)</li>
     *  <li>urn:docflow-common-status:delivered - от КО пришел документ, подтверждающий доставку документов до сотрудника КО</li>
     *  <li>urn:docflow-common-status:response-arrived - пришли результаты проверки отправленного документа</li>
     *  <li>urn:docflow-common-status:response-processed - пришли результаты обработки отправленного документа</li>
     *  <li>urn:urn:docflow-common-status:received - документ получен КО</li>
     *  <li>urn:docflow-common-status:arrived - документ получен налогоплательщиком</li>
     *  <li>urn:docflow-common-status:processed - налогоплательщик отправил результат приема документа КО</li>
     *  <li>urn:docflow-common-status:finished - завершен полный цикл ДО, в нем есть все транзакции, предусмотренные регламентом</li>
     * </ul>
     */
    public void setStatus(DocflowStatus status) {
        this.status = status;
    }

    /**
     * Возвращает дескриптор ДО {@link IDocflowDescription}
     * @return description дескриптор ДО
     * @see DocflowStatus
     */
    public IDocflowDescription getDescription() {
        return description;
    }

    /**
     * Устанавливает дескриптор ДО {@link IDocflowDescription}
     * @param description дескриптор ДО
     * @see IDocflowDescription
     */
    public void setDescription(IDocflowDescription description) {
        this.description = description;
    }

    /**
     * Возвращает список документов ДО {@link Document}.
     * @return documents список документов ДО.
     * @see Document
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * Устанавливает список документов ДО {@link Document}.
     * @param documents список документов ДО.
     * @see Document
     */
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    /**
     * Возвращает список ссылок {@link Link}.
     * Список может содержать ссылки на:
     * <ul>
     *   <li>ДО с типом self</li>
     *   <li>документы с типом reply, которые необходимо получить и подписать</li>
     * </ul>
     * @return links
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок {@link Link}.
     * @param links список ссылок:
     * <ul>
     *   <li>ДО с типом self</li>
     *   <li>документы с типом reply, которые необходимо получить и подписать</li>
     * </ul>
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Возвращает дату отправки черновика в контролирующий орган
     * @return sendDate дата отправки черновика в контролирующий орган
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * Устанавливает дату отправки черновика в контролирующий орган
     * @param sendDate дата отправки черновика в контролирующий орган
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Возвращает дату последнего обновления ДО
     * @return lastChangeDate дата последнего обновления ДО
     */
    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    /**
     * Устанавливает дату последнего обновления ДО
     * @param lastChangeDate дата последнего обновления ДО
     */
    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
