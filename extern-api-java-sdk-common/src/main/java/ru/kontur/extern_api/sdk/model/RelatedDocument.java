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

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RelatedDocument {
    private UUID relatedDocflowId;
    private UUID relatedDocumentId;

    public RelatedDocument(){

    }

    public RelatedDocument(UUID relatedDocflowId, UUID relatedDocumentId){
        this.relatedDocflowId = relatedDocflowId;
        this.relatedDocumentId = relatedDocumentId;
    }

    public RelatedDocument(@NotNull Docflow relatedDocflow, @NotNull Document relatedDocument){
        this.relatedDocflowId = relatedDocflow.getId();
        this.relatedDocumentId = relatedDocument.getId();
    }

    /**
     * @return объект {@link UUID}, связанного документооборота
     */
    public UUID getRelatedDocflowId() { return relatedDocflowId; }

    /**
     * Устанавливает объект {@link UUID}, связанного документооборота
     * @param relatedDocflowId объект {@link UUID}, связанного документооборота
     */
    public void setRelatedDocflowId(UUID relatedDocflowId) {
        this.relatedDocflowId = relatedDocflowId;
    }

    /**
     * @return объект {@link UUID}, связанного документа
     */
    public UUID getRelatedDocumentId() { return relatedDocumentId; }

    /**
     * Устанавливает объект {@link UUID}, связанного документа
     * @param relatedDocumentId объект {@link UUID}, связанного документа
     */
    public void setRelatedDocumentId(UUID relatedDocumentId) {
        this.relatedDocumentId = relatedDocumentId;
    }

}
