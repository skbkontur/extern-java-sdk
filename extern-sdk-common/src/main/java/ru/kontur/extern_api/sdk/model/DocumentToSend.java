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

import java.util.List;
import java.util.UUID;


/**
 * <p>
 *     Класс содержит данные, необходимые для отправки документа
 * </p>
 * @author Aleksey Sukhorukov
 */
public class DocumentToSend {

    @SerializedName("id")
    private UUID id = null;
    @SerializedName("content")
    private byte[] content = null;
    @SerializedName("filename")
    private String filename = null;
    @SerializedName("signature")
    private byte[] signature = null;
    @SerializedName("links")
    private List<Link> links = null;


    /**
     * Возврашает идентификатор документа
     * @return id идентификатор документа
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор документа
     * @param id String идентификатор документа
     */
    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    /**
     * Устанавливает идентификатор документа
     * @param id {@link UUID} идентификатор документа
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает содержимое документа
     * @return content byte array содержимое документа
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Устанавливает содержимое документа
     * @param content byte array содержимое документа
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * Возвращает имя файла документа
     * @return filename имя файла документа
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Устанавливает имя файла документа
     * @param filename имя файла документа
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Возвращает структуру данных с подписью
     * @return signature структура данных с подписью
     * @see SignatureToSend
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Устанавливает структуру данных с подписью
     * @param signature структуру данных с подписью
     * @see SignatureToSend
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Возвращает список ссылок
     * @return список ссылок
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок
     * @param links список ссылок
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
