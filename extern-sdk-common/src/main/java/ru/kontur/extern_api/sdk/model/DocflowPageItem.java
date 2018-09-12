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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * <p>
 * Класс является элементом списка {@link DocflowPage} и представляет документооборот (ДО)
 * </p>
 * @author Aleksey Sukhorukov
 */
public class DocflowPageItem {

    private UUID id = null;
    private DocflowType type = null;
    private DocflowStatus status = null;
    private List<Link> links = new ArrayList<>();
    private Date sendDate = null;
    private Date lastChangeDate = null;

    /**
     * Возвращает идентификатор ДО
     * @return идентификатор ДО
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
     * @return тип ДО
     */
    public DocflowType getType() {
        return type;
    }

    /**
     * Устанавливает тип ДО
     * @param type тип ДО:
     * <ul>
     *   <li>urn:docflow:fns534-report</li>
     * </ul>
     */
    public void setType(DocflowType type) {
        this.type = type;
    }

    /**
     * Возвращает состояние ДО
     * <ul>
     *   <li>urn:docflow-common-status:sent - документы были отправлены в контролирующий орган (КО)</li>
     *   <li>urn:docflow-common-status:delivered - от КО пришел документ, подтверждающий доставку документов до сотрудника КО</li>
     *   <li>urn:docflow-common-status:response-arrived - пришли результаты проверки отправленного документа</li>
     *   <li>urn:docflow-common-status:response-processed - пришли результаты обработки отправленного документа</li>
     *   <li>urn:urn:docflow-common-status:received - документ получен КО</li>
     *   <li>urn:docflow-common-status:arrived - документ получен налогоплательщиком</li>
     *   <li>urn:docflow-common-status:processed - налогоплательщик отправил результат приема документа КО</li>
     *   <li>urn:docflow-common-status:finished - завершен полный цикл ДО, в нем есть все транзакции, предусмотренные регламентом</li>
     * </ul>
     * @return состояние ДО
     */
    public DocflowStatus getStatus() {
        return status;
    }

    /**
     * Устанавливает состояние ДО
     * @param status состояние ДО:
     * <ul>
     *   <li>urn:docflow-common-status:sent - документы были отправлены в контролирующий орган (КО)</li>
     *   <li>urn:docflow-common-status:delivered - от КО пришел документ, подтверждающий доставку документов до сотрудника КО</li>
     *   <li>urn:docflow-common-status:response-arrived - пришли результаты проверки отправленного документа</li>
     *   <li>urn:docflow-common-status:response-processed - пришли результаты обработки отправленного документа</li>
     *   <li>urn:urn:docflow-common-status:received - документ получен КО</li>
     *   <li>urn:docflow-common-status:arrived - документ получен налогоплательщиком</li>
     *   <li>urn:docflow-common-status:processed - налогоплательщик отправил результат приема документа КО</li>
     *   <li>urn:docflow-common-status:finished - завершен полный цикл ДО, в нем есть все транзакции, предусмотренные регламентом</li>
     * </ul>
     */
    public void setStatus(DocflowStatus status) {
        this.status = status;
    }

    /**
     * Возвращает список ссылок на документообороты
     * @return список ссылок на документообороты
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок на документообороты
     * @param links список ссылок на документообороты
     * @see  Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Возвращает дату отправки ДО
     * @return дата отправки ДО
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * Устанавливает дату отправки ДО
     * @param sendDate дата отправки ДО
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Возвращает дату обновления ДО
     * @return дата обновления ДО
     */
    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    /**
     * Устанавливает дату обновления ДО
     * @param lastChangeDate дата обновления ДО
     */
    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
