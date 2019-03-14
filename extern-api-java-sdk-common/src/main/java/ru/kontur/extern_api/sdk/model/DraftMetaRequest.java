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


/**
 *  Класс содержит информацию для создания черновика
 */
public class DraftMetaRequest {
    private SenderRequest sender;
    private Recipient recipient;
    private OrganizationRequest payer;
    private RelatedDocumentRequest relatedDocument;

    public DraftMetaRequest() {
    }

    public DraftMetaRequest(SenderRequest sender, Recipient recipient, OrganizationRequest payer) {
        this.sender = sender;
        this.recipient = recipient;
        this.payer = payer;
    }

    /**
     * @return объект {@link SenderRequest}, описывающий отправителя документа
     */
    public SenderRequest getSender() { return sender; }

    /**
     * Устанавливает объект {@link SenderRequest}, описывающий отправителя документа
     * @param sender объект, описывающий отправителя документа
     */
    public void setSender(SenderRequest sender) {
        this.sender = sender;
    }

    /**
     * @return объект {@link FnsRecipient}, описывающий получателя документа
     */
    public Recipient getRecipient() { return recipient; }

    /**
     * Устанавливает объект {@link FnsRecipient}, описывающий получателя документа
     * @param recipient объект {@link FnsRecipient}, описывающий получателя документа
     */
    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    /**
     * @return объект {@link OrganizationRequest}, описывающий организацию, за которую производится сдача документа
     */
    public OrganizationRequest getPayer() { return payer; }

    /**
     * Устанавливает объект {@link OrganizationRequest}, описывающий организацию, за которую производится сдача документа
     * @param payer объект {@link OrganizationRequest}, описывающий организацию, за которую производится сдача документа
     */
    public void setPayer(OrganizationRequest payer) {
        this.payer = payer;
    }

    /**
     * Возвращает связный ДО
     * @return связный ДО
     */
    public RelatedDocumentRequest getRelatedDocument() { return relatedDocument; }

    /**
     * Устанавливает связный ДО
     * @param relatedDocument связный ДО
     */
    public void setRelatedDocument(RelatedDocumentRequest relatedDocument) { this.relatedDocument = relatedDocument; }
}
