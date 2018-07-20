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
import java.util.List;
import java.util.UUID;


/**
 * @author AlexS
 * Класс предоставляет информацию о документообороте (ДО).
 * Используется в сервисах: {@code DraftService} и {@code DocflowService}.
 */
public class Docflow {

    private UUID id = null;
    private String type = null;
    private String status = null;
    private DocflowDescription description = null;
    private List<Document> documents = null;
    private List<Link> links = null;
    @SerializedName("send-date")
    private Date sendDate = null;
    @SerializedName("last-change-date")
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
     * <li>urn:docflow:fns534-report</li>
     * @return type тип ДО
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип ДО:
     * <li>urn:docflow-type:ke-fns-report</li>
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Возвращает состояние документооборота:
     *  <li>urn:docflow-common-status:sent - документы были отправлены в контролирующий орган (КО)</li>
     *  <li>urn:docflow-common-status:delivered - от КО пришел документ, подтверждающий доставку документов до сотрудника КО</li>
     *  <li>urn:docflow-common-status:response-arrived - пришли результаты проверки отправленного документа</li>
     *  <li>urn:docflow-common-status:response-processed - пришли результаты обработки отправленного документа</li>
     *  <li>urn:docflow-common-status:received - документ получен КО</li>
     *  <li>urn:docflow-common-status:arrived - документ получен налогоплательщиком</li>
     *  <li>urn:docflow-common-status:processed - налогоплательщик отправил результат приема документа КО</li>
     *  <li>urn:docflow-common-status:finished - завершен полный цикл ДО, в нем есть все транзакции, предусмотренные регламентом</li>
     * @return состояние документооборота
     */
    public String getStatus() {
        return status;
    }

    /**
     * Устанавливает состояние документооборота
     *  <li>urn:docflow-common-status:sent - документы были отправлены в контролирующий орган (КО)</li>
     *  <li>urn:docflow-common-status:delivered - от КО пришел документ, подтверждающий доставку документов до сотрудника КО</li>
     *  <li>urn:docflow-common-status:response-arrived - пришли результаты проверки отправленного документа</li>
     *  <li>urn:docflow-common-status:response-processed - пришли результаты обработки отправленного документа</li>
     *  <li>urn:urn:docflow-common-status:received - документ получен КО</li>
     *  <li>urn:docflow-common-status:arrived - документ получен налогоплательщиком</li>
     *  <li>urn:docflow-common-status:processed - налогоплательщик отправил результат приема документа КО</li>
     *  <li>urn:docflow-common-status:finished - завершен полный цикл ДО, в нем есть все транзакции, предусмотренные регламентом</li>
     * @param status состояние документооборота
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Возвращает дескриптор ДО {@link DocflowDescription}
     * @return description дескриптор ДО
     * @see DocflowDescription
     */
    public DocflowDescription getDescription() {
        return description;
    }

    /**
     * Устанавливает дескриптор ДО {@link DocflowDescription}
     * @param description дескриптор ДО
     * @see DocflowDescription
     */
    public void setDescription(DocflowDescription description) {
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
     * <li>ДО с типом self</li>
     * <li>документы с типом reply, которые необходимо получить и подписать</li>
     * @return links
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок {@link Link}.
     * Список может содержать ссылки на:
     * <li>ДО с типом self</li>
     * <li>документы с типом reply, которые необходимо получить и подписать</li>
     * @param links список ссылок
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
     * @param lastChangeDate
     */
    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
