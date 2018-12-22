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
 *     Класс содержит информацию о черновике
 * </p>
 * @author Aleksey Sukhorukov
 */
public class DraftMeta {

    private Sender sender;
    private Recipient recipient;
    private AccountInfoRequest payer;

    public DraftMeta() {
    }

    public DraftMeta(Sender sender, Recipient recipient, AccountInfoRequest payer) {
        this.sender = sender;
        this.recipient = recipient;
        this.payer = payer;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public AccountInfoRequest getPayer() {
        return payer;
    }

    public void setPayer(AccountInfoRequest payer) {
        this.payer = payer;
    }

    public DraftMetaRequest asRequest() {
        Sender sender = this.getSender();
        return new DraftMetaRequest(
                new SenderRequest(
                        sender.getInn(),
                        sender.getKpp(),
                        sender.getCertificate(),
                        sender.getIpaddress()
                ),
                getRecipient(),
                new AccountInfoRequest(getPayer().getInn(), getPayer().getKpp())
        );


    }
}
