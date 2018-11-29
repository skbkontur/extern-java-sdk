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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * <p>Объект данного класс возвращается сервисом облачной криптографии для подтверждения операции подписантом</p>
 */
public class SignInitiation {

    private List<Link> links;
    private List<Link> documentsToSign;
    private String requestId;

    /**
     * <p>Возвращает список ссылок на сущности</p>
     * @return список ссылок на сущности
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * <p>Устанавливает список ссылок на сущности. Устанавливается сервисом.</p>
     * @param links список ссылок на сущности
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * <p>Возвращает список ссылок на документы, которые будут подписаны.</p>
     * @return список ссылок на документы, которые будут подписаны
     * @see Link
     */
    public List<Link> getDocumentsToSign() {
        return documentsToSign;
    }

    /**
     * <p>Устанавливает список ссылок на документы, которые будут подписаны. Устанавливается сервисом.</p>
     * @param documentsToSign список ссылок на документы, которые будут подписаны
     * @see Link
     */
    public void setDocumentsToSign(List<Link> documentsToSign) {
        this.documentsToSign = documentsToSign;
    }

    /**
     * <p>Возвращает идентификатор запроса на подпись</p>
     * @return идентификатор запроса на подпись
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * <p>Устанавливает идентификатор запроса на подпись</p>
     * @param requestId идентификатор запроса на подпись
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean needToConfirmSigning() {
        return requestId != null;
    }
}
