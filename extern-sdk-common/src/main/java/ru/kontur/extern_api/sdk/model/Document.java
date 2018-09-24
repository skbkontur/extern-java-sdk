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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;


/**
 * <p>
 * Класс содержит информацию документа документооборота
 * </p>
 */
public class Document {

    private UUID id = null;
    private DocflowDocumentDescription description = null;
    private Content content = null;
    private Date sendDate = null;
    private List<Signature> signatures = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public Document id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Возвращает идентификатор документа
     *
     * @return id идентификатор документа
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор документа
     *
     * @param id идентификатор документа
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает дескриптор документа {@link DocumentDescription}
     *
     * @return description дескриптор документа
     */
    public DocflowDocumentDescription getDescription() {
        return description;
    }

    /**
     * Устанавливает дескриптор документа {@link DocumentDescription}
     *
     * @param description дескриптор документа
     */
    public void setDescription(DocflowDocumentDescription description) {
        this.description = description;
    }

    /**
     * Возвращает контент документа {@link Content}
     *
     * @return content контент документа
     * @see Content
     */
    public Content getContent() {
        return content;
    }

    /**
     * Устанавливает контент документа {@link Content}
     *
     * @param content контент документа
     * @see Content
     */
    public void setContent(Content content) {
        this.content = content;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Возвращает список подписей документа {@link Signature}
     *
     * @return signatures список подписей
     * @see Signature
     */
    public List<Signature> getSignatures() {
        return signatures;
    }

    /**
     * Устанавливает список подписей документа {@link Signature}
     *
     * @param signatures список подписей документа
     * @see Signature
     */
    public void setSignatures(List<Signature> signatures) {
        this.signatures = signatures;
    }

    /**
     * Возвращает список ссылок на ресурсы документооборота {@link Link}
     *
     * @return links список ссылок на ресурсы документооборота
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок на ресурсы документооборота {@link Link}
     *
     * @param links список ссылок на ресурсы документооборота
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public boolean isNeedToReply() {
        return getLinks().stream().anyMatch(link -> Objects.equals(link.getRel(), "reply"));
    }

    /**
     * @return Сылки на генерацию ответных документов к данному документу. {@link Link#getName()}
     *         содержит тип ответного документа. Для генерации ответного документа удобно
     *         использовать
     *         {@link HttpClient#followPostLink(String, Object, Class)} c аргуметами {@link
     *         Link#getHref()},
     *         {@link GenerateReplyDocumentRequestData} и {@link ReplyDocument#getClass()}
     */
    public Link[] getReplyLinks() {
        return getLinks()
                .stream()
                .filter(link -> Objects.equals(link.getRel(), "reply"))
                .toArray(Link[]::new);
    }

    public String[] getReplyOptions() {
        return Arrays
                .stream(getReplyLinks())
                .map(Link::getName)
                .toArray(String[]::new);
    }
}
