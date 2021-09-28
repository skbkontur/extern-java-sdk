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

import java.util.Collection;
import java.util.UUID;

/**
 * <p>
 *     Класс содержит описание документа черновика
 * </p>
 * @author Aleksey Sukhorukov
 */
public class DraftDocument {

    private UUID id = null;
    private Link decryptedContentLink = null;
    private Link encryptedContentLink = null;
    private Link signatureContentLink = null;
    private DocumentDescription description = null;
    private Collection<DraftDocumentContent> contents = null;

    /**
     * Возвращает идентификатор документа
     * @return id идентификатор документа
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор документа
     * @param id UUID идентификатор документа
     * @see UUID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает ссылку на расшифрованный контент документа
     * @return ссылка на расшифрованный контент документа
     * @see Link
     */
    public Link getDecryptedContentLink() {
        return decryptedContentLink;
    }

    /**
     * Устанавливает ссылку на расшифрованный контент документа
     * @param decryptedContentLink ссылка на расшифрованный контент документа
     * @see Link
     */
    public void setDecryptedContentLink(Link decryptedContentLink) {
        this.decryptedContentLink = decryptedContentLink;
    }

    /**
     * Возвращает ссылку на зашифрованный контент документа
     * @return ссылка на зашифрованный контент документа
     * @see Link
     */
    public Link getEncryptedContentLink() {
        return encryptedContentLink;
    }

    /**
     * Устанавливает ссылку на зашифрованный контент документа
     * @param encryptedContentLink ссылка на зашифрованный контент документа
     * @see Link
     */
    public void setEncryptedContentLink(Link encryptedContentLink) {
        this.encryptedContentLink = encryptedContentLink;
    }

    /**
     * Возвращает ссылку на подпись документа
     * @return signatureContentLink ссылка на подпись документа
     * @see Link
     */
    public Link getSignatureContentLink() {
        return signatureContentLink;
    }

    /**
     * Устанавливает ссылку на подпись документа
     * @param signatureContentLink ссылка на подпись документа
     * @see Link
     */
    public void setSignatureContentLink(Link signatureContentLink) {
        this.signatureContentLink = signatureContentLink;
    }

    /**
     * Возвращает дескриптор документа
     * @return дескриптор документа
     * @see DocumentDescription
     */
    public DocumentDescription getDescription() {
        return description;
    }

    /**
     * Устанавливает дескриптор документа
     * @param description дескриптор документа
     * @see DocumentDescription
     */
    public void setDescription(DocumentDescription description) {
        this.description = description;
    }

    /**
     * Возвращает коллекцию с идентификаторами контента черновика
     * @return коллекция с идентификаторами контента
     * @see DraftDocumentContent
     */
    public Collection<DraftDocumentContent> getContents() {
        return contents;
    }

    /**
     * Устанавливает коллекцию с идентификаторами контента черновика
     * @param contents коллекция с идентификаторами контента
     * @see DocumentDescription
     */
    public void setContents(Collection<DraftDocumentContent> contents) {
        this.contents = contents;
    }
}
