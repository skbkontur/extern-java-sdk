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
 * Класс содержит описания черновика.
 * </p>
 *
 * @author Aleksey Sukhorukov
 */
public class Draft {

    private UUID id;

    private List<Link> docflows = null;

    private List<Link> documents = null;

    private DraftMeta meta = null;

    private StatusEnum status = null;

    public Draft() {
    }

    public Draft(UUID id, StatusEnum status) {
        this.id = id;
        this.status = status;
    }

    /**
     * Возвращает идентификатор черновика
     *
     * @return идентификатор черновика
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор черновика
     *
     * @param id идентификатор черновика
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает статус черновика {@link StatusEnum}:
     * <ul>
     * <li>NEW - создан новый черновик</li>
     * <li>CHECKED - черновик был проверен</li>
     * <li>READYTOSEND - черновик был проверен и подготовлен к отправке</li>
     * <li>SENT - черновик был отправлен</li>
     * </ul>
     *
     * @return статус черновика
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * Устанавливает статус черновика
     *
     * @param status String статус черновика:
     *         <ul>
     *         <li>NEW - создан новый черновик</li>
     *         <li>CHECKED - черновик был проверен</li>
     *         <li>READYTOSEND - черновик был проверен и подготовлен к отправке</li>
     *         <li>SENT - черновик был отправлен</li>
     *         </ul>
     * @see StatusEnum
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * Возвращает мета-данные черновика
     *
     * @return мета-данные черновика
     * @see DraftMeta
     */
    public DraftMeta getMeta() {
        return meta;
    }

    public void setMeta(DraftMeta draftMeta) {
        meta = draftMeta;
    }

    public enum StatusEnum {
        @SerializedName("new")
        NEW,
        @SerializedName("checked")
        CHECKED,
        @SerializedName("ready-to-send")
        READYTOSEND,
        @SerializedName("sent")
        SENT;
    }

    public List<Link> getDocflows() {
        return docflows;
    }

    public List<Link> getDocuments() {
        return documents;
    }
}
