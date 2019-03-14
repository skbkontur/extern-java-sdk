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
 * <p>
 *     Класс содержит информацию о билдере черновиков
 * </p>
 */
public class DraftsBuilderMeta {
    private Sender sender;
    private Recipient recipient;
    private Organization payer;
    private String builderType;
    private DraftsBuilderData builderData;

    public DraftsBuilderMeta() {
    }

    public DraftsBuilderMeta(Sender sender, Recipient recipient, Organization payer, String builderType, DraftsBuilderData builderData) {
        this.sender = sender;
        this.recipient = recipient;
        this.payer = payer;
        this.builderType = builderType;
        this.builderData = builderData;
    }

    /**
     * Возвращает объект {@link Sender}, описывающий отправителя документа
     * @return объект, описывающий отправителя документа
     * @see Sender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Устанавливает объект {@link Sender}, описывающий отправителя документа
     * @param sender объект, описывающий отправителя документа
     * @see Sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * Возвращает объект {@link FnsRecipient}, описывающий получателя документа
     * @return объект {@link FnsRecipient}, описывающий получателя документа
     * @see FnsRecipient
     */
    public Recipient getRecipient() {
        return recipient;
    }

    /**
     * Устанавливает объект {@link FnsRecipient}, описывающий получателя документа
     * @param recipient объект {@link FnsRecipient}, описывающий получателя документа
     * @see FnsRecipient
     */
    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    /**
     * Возвращает объект {@link Organization}, описывающий организацию, за которую производится сдача документа
     * @return объект, описывающий организацию, за которую производится сдача документа
     * @see Organization
     */
    public Organization getPayer() {
        return payer;
    }

    /**
     * Устанавливает объект {@link Organization}, описывающий организацию, за которую производится сдача документа
     * @param payer объект {@link Organization}, описывающий организацию, за которую производится сдача документа
     * @see Organization
     */
    public void setPayer(Organization payer) { this.payer = payer; }

    /**
     * Возвращает тип билдера черновиков. Могут быть следующие типы билдера черновиков:
     * <ul>
     *   <li>urn:ke.api.public:fns:submission - представление ФНС</li>
     * </ul>
     * @return тип билдера черновиков
     */
    public String getBuilderType() { return builderType; }

    /**
     * Устанавливает тип билдера черновиков. Могут быть следующие типы билдера черновиков:
     * @param type тип билдера черновиков
     * <ul>
     *   <li>urn:ke.api.public:fns:submission - представление ФНС</li>
     * </ul>
     */
    public void setBuilderType(String type) { this.builderType = type; }

    /**
     * Возвращает объект {@link DraftsBuilderData}, содержащий дополнительные данные для указанного типа билдера черновиков
     * @return объект, содержащий дополнительные данные для указанного типа билдера черновиков
     * @see DraftsBuilderData
     */
    public DraftsBuilderData getBuilderData() { return builderData; }

    /**
     * Устанавливает объект {@link DraftsBuilderData}, содержащий дополнительные данные для указанного типа билдера черновиков
     * @param builderData объект {@link DraftsBuilderData}, содержащий дополнительные данные для указанного типа билдера черновиков
     * @see DraftsBuilderData
     */
    public void setBuilderData(DraftsBuilderData builderData) { this.builderData = builderData; }

    public DraftsBuilderMetaRequest asRequest() {
        Sender sender = this.getSender();
        return new DraftsBuilderMetaRequest(
            new SenderRequest(
                    sender.getInn(),
                    sender.getKpp(),
                    sender.getCertificate(),
                    sender.getIpaddress()
            ),
            getRecipient(),
            new OrganizationRequest(getPayer().getInn(), getPayer().getKpp(), getPayer().getName()),
            getBuilderType(),
            getBuilderData()
        );
    }
}
