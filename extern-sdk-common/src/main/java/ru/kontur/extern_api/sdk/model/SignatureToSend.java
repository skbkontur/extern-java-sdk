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

import java.util.UUID;


/**
 * <p>Класс, содержащий информацию для отправки контента подписи</p>
 * @author Aleksey Sukhorukov
 */
public class SignatureToSend {

    @SerializedName("id")
    private UUID id = null;
    @SerializedName("content-data")
    private byte[] contentData = null;

    /**
     * <p>Возвращает идентификатор подписи. Инициализуруется сервером.</p>
     * @return id идентификатор подписи
     */
    public UUID getId() {
        return id;
    }

    /**
     * <p>Устанавливает идентификатор подписи. Инициализуруется сервером.</p>
     * @param id String идентификатор подписи
     */
    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    /**
     * <p>Устанавливает идентификатор подписи. Инициализуруется сервером.</p>
     * @param id UUID идентификатор подписи
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает массив байт подписи в формате PKCS#7
     * @return contentData массив байт подписи в формате PKCS#7
     */
    public byte[] getContentData() {
        return contentData;
    }

    /**
     * Устанавливает массив байт подписи в формате PKCS#7
     * @param contentData массив байт подписи в формате PKCS#7
     */
    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
    }
}
