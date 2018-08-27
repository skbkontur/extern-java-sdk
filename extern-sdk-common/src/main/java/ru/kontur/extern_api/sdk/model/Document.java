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
import java.util.List;
import java.util.UUID;


/**
 * <p>
 * Класс содержит информацию документа документооборота
 * </p>
 * @author Aleksey Sukhorukov
 */
public class Document {

    private UUID id = null;
    private DocflowDocumentDescription description = null;
    private Content content = null;
    private List<Signature> signatures = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public Document id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Возвращает идентификатор документа
     * @return id идентификатор документа
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор документа
     * @param id идентификатор документа
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Устанавливает дескриптор документа {@link DocumentDescription}
     * @param description дескриптор документа
     * @return {@link Document}
     */
    public Document description(DocflowDocumentDescription description) {
        this.description = description;
        return this;
    }

    /**
     * Возвращает дескриптор документа {@link DocumentDescription}
     * @return description дескриптор документа
     */
    public DocflowDocumentDescription getDescription() {
        return description;
    }

    /**
     * Устанавливает дескриптор документа {@link DocumentDescription}
     * @param description дескриптор документа
     */
    public void setDescription(DocflowDocumentDescription description) {
        this.description = description;
    }

    /**
     * Устанавливает контент документа {@link Content}
     * @param content контент документа
     * @return {@link Document}
     */
    public Document content(Content content) {
        this.content = content;
        return this;
    }

    /**
     * Возвращает контент документа {@link Content}
     * @return content контент документа
     * @see Content
     */
    public Content getContent() {
        return content;
    }

    /**
     * Устанавливает контент документа {@link Content}
     * @param content контент документа
     * @see Content
     */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Устанавливает список подписей документа {@link Signature}
     * @param signatures список подписей документа
     * @return {@link Document}
     */
    public Document signatures(List<Signature> signatures) {
        this.signatures = signatures;
        return this;
    }

    /**
     * Возвращает список подписей документа {@link Signature}
     * @return signatures список подписей
     * @see Signature
     */
    public List<Signature> getSignatures() {
        return signatures;
    }

    /**
     * Устанавливает список подписей документа {@link Signature}
     * @param signatures список подписей документа
     * @see Signature
     */
    public void setSignatures(List<Signature> signatures) {
        this.signatures = signatures;
    }

    /**
     * Устанавливает список ссылок на ресурсы документооборота {@link Link}
     * @param links список ссылок на ресурсы документооборота
     * @return {@link Document}
     */
    public Document links(List<Link> links) {
        this.links = links;
        return this;
    }

    /**
     * Возвращает список ссылок на ресурсы документооборота {@link Link}
     * @return links список ссылок на ресурсы документооборота
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок на ресурсы документооборота {@link Link}
     * @param links список ссылок на ресурсы документооборота
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
