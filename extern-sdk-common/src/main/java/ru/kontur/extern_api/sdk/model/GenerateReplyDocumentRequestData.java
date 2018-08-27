/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 *     Класс содержит сертификат подписанта в кодировке BASE64
 * </p>
 * @author Mikhail Pavlenko
 */

public class GenerateReplyDocumentRequestData {

    private String certificateBase64 = null;

    public GenerateReplyDocumentRequestData certificateBase64(String certificateBase64) {
        this.certificateBase64 = certificateBase64;
        return this;
    }

    /**
     * <p>Возвращает сертификат в кодировке BASE64</p>
     *
     * @return сертификат в кодировке BASE64
     */
    public String getCertificateBase64() {
        return certificateBase64;
    }

    /**
     * <p>Устанавливает сертификат в кодировке BASE64</p>
     *
     * @param certificateBase64 сертификат в кодировке BASE64
     */
    public void setCertificateBase64(String certificateBase64) {
        this.certificateBase64 = certificateBase64;
    }
}
