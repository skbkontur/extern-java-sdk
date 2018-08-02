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

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * <p>Класс содержит информацию о подписи документа</p>
 * @author Aleksey Sukhorukov
 */
public class Signature {

    private UUID id = null;
    @SerializedName("content-link")
    private Link contentLink = null;
    private List<Link> links = new ArrayList<>();

    public Signature id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * <p>Возвращает идентификатор подписи</p>
     * @return id идентификатор подписи
     */
    public UUID getId() {
        return id;
    }

    /**
     * <p>Устанавливает идентификатор подписи</p>
     * @param id идентификатор подписи
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * <p>Устанавливает ссылку на подпись</p>
     * @param contentLink ссылка {@link Link} на подпись
     * @return подпись
     */
    public Signature contentLink(Link contentLink) {
        this.contentLink = contentLink;
        return this;
    }

    /**
     * <p>Возвращает ссылку на контент подписи</p>
     * @return contentLink ссылка {@link Link} на подпись
     */
    public Link getContentLink() {
        return contentLink;
    }

    /**
     * <p>Устанавливает ссылку на контент подписи</p>
     * @param contentLink ссылка {@link Link} на подпись
     */
    public void setContentLink(Link contentLink) {
        this.contentLink = contentLink;
    }

    /**
     * <p>Устанавливает ссылки на ресурсы</p>
     * @param links ссылки на ресурсы
     * @return подпись
     */
    public Signature links(List<Link> links) {
        this.links = links;
        return this;
    }

    /**
     * <p>Возвращает ссылки на сущности документооборота</p>
     * @return links
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * <p>Устанавливает ссылки на сущности документооборота</p>
     * @param links ссылки на сущности документооборота
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
