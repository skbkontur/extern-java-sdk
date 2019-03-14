/*
 * Copyright (c) 2019 SKB Kontur
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

import java.util.UUID;

public class DraftsBuilderDocumentFile {
    private UUID id;
    private UUID draftsBuilderId;
    private UUID draftsBuilderDocumentId;
    private Link contentLink;
    private Link signatureContentLink;
    private DraftsBuilderDocumentFileMeta meta;

    /**
     * Возвращает идентификатор файла документа билдера черновиков
     * @return идентификатор файла документа билдера черновиков
     */
    public UUID getId() { return id; }

    /**
     * Устанавливает идентификатор файла документа билдера черновиков
     * @param id идентификатор файла документа билдера черновиков
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Возвращает идентификатор билдера черновиков
     * @return идентификатор билдера черновиков
     */
    public UUID getDraftsBuilderId() { return draftsBuilderId; }

    /**
     * Устанавливает идентификатор билдера черновиков
     * @param draftsBuilderId идентификатор билдера черновиков
     */
    public void setDraftsBuilderId(UUID draftsBuilderId) { this.draftsBuilderId = draftsBuilderId; }

    /**
     * Возвращает идентификатор документа билдера черновиков
     * @return идентификатор документа билдера черновиков
     */
    public UUID getDraftsBuilderDocumentId() { return draftsBuilderDocumentId; }

    /**
     * Устанавливает идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     */
    public void setDraftsBuilderDocumentId(UUID draftsBuilderDocumentId) { this.draftsBuilderDocumentId = draftsBuilderDocumentId; }

    /**
     * Возвращает ссылку на контент файла
     * @return ссылку на контент файла
     */
    public Link getContentLink() { return contentLink; }

    /**
     * Устанавливает ссылку на контент файла
     * @param contentLink ссылку на контент файла
     */
    public void setContentLink(Link contentLink) { this.contentLink = contentLink; }

    /**
     * Возвращает ссылку на контент подписи файла
     * @return ссылку на контент подписи файла
     */
    public Link getSignatureContentLink() { return signatureContentLink; }

    /**
     * Устанавливает ссылку на контент подписи файла
     * @param signatureContentLink ссылку на контент подписи файла
     */
    public void setSignatureContentLink(Link signatureContentLink) { this.signatureContentLink = signatureContentLink; }

    /**
     * Возвращает мета-данные файла документа билдера черновиков
     * @return мета-данные файла документа билдера черновиков
     */
    public DraftsBuilderDocumentFileMeta getMeta() { return meta; }

    /**
     * Устанавливает мета-данные файла документа билдера черновиков
     * @param meta мета-данные файла документа билдера черновиков
     */
    public void setMeta(DraftsBuilderDocumentFileMeta meta) { this.meta = meta; }
}
