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

package ru.kontur.extern_api.sdk.model.warrants;

import java.util.Date;
import java.util.List;

/**
 * Доверенность
 */
public class Warrant {

    private Date dateBegin = null;
    private Date dateEnd = null;
    private String number = null;
    private List<Integer> permissions = null;
    private Notary notary = null;
    private WarrantSender sender = null;
    private WarrantIssuer issuer = null;
    private WarrantTrustedIssuer trustedIssuer = null;

    /**
     * Дата начала действия доверенности
     *
     * @return dateBegin
     **/
    public Date getDateBegin() {
        return dateBegin;
    }

    /**
     * Дата окончания действия доверенности
     *
     * @return dateEnd
     **/
    public Date getDateEnd() {
        return dateEnd;
    }

    /**
     * Номер доверенности
     *
     * @return number
     **/
    public String getNumber() {
        return number;
    }

    /**
     * Список полномочий представителя
     *
     * @return permissions
     **/
    public List<Integer> getPermissions() {
        return permissions;
    }

    /**
     * Нотариус
     *
     * @return notary
     **/
    public Notary getNotary() {
        return notary;
    }

    /**
     * Уполномоченный представитель, на которого выдана доверенность (отправитель)
     *
     * @return sender
     **/
    public WarrantSender getSender() {
        return sender;
    }

    /**
     * Представляемое лицо
     *
     * @return issuer
     **/
    public WarrantIssuer getIssuer() {
        return issuer;
    }

    /**
     * Представитель, который выдал доверенность.
     * Присутствует в случае передоверия, когда доверенность выдана не самим представляемым лицом, а его
     * представителем.
     * Отсутствует в случае, когда лицо, выдавшее доверенность, (доверитель) совпадает с представляемым
     * лицом.
     *
     * @return trustedIssuer
     **/
    public WarrantTrustedIssuer getTrustedIssuer() {
        return trustedIssuer;
    }
}

