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

import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.Sender;


/**
 *  Класс содержит информацию для создания черновике
 */
public class CreateDraftMeta {

    private CreateSender sender;
    private Recipient recipient;
    private CreateOrganization payer;

    public CreateDraftMeta() {
    }

    public CreateDraftMeta(CreateSender sender, Recipient recipient, CreateOrganization payer) {
        this.sender = sender;
        this.recipient = recipient;
        this.payer = payer;
    }

    /**
     * Возвращает объект {@link CreateSender}, описывающий отправителя документа
     */
    public CreateSender getSender() { return sender; }

    /**
     * Устанавливает объект {@link CreateSender}, описывающий отправителя документа
     * @param sender объект, описывающий отправителя документа
     */
    public void setSender(CreateSender sender) {
        this.sender = sender;
    }

    /**
     * Возвращает объект {@link FnsRecipient}, описывающий получателя документа
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
     * Возвращает объект {@link CreateOrganization}, описывающий организацию, за которую производится сдача документа
     */
    public CreateOrganization getPayer() { return payer; }

    /**
     * Устанавливает объект {@link CreateOrganization}, описывающий организацию, за которую производится сдача документа
     * @param payer объект {@link CreateOrganization}, описывающий организацию, за которую производится сдача документа
     */
    public void setPayer(CreateOrganization payer) {
        this.payer = payer;
    }
}
