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

/**
 * <p>
 * Класс предназначен для передачи контента документа и его подписи
 * </p>
 * @author Aleksey Sukhorukov
 */
public class DocumentContents {

    @SerializedName("base64-content")
    private String base64Content = null;
    private String signature = null;
    @SerializedName("document-description")
    private DocumentDescription documentDescription = null;

    /**
     * Возвращает контент незашифрованного документа в кодировке BASE64
     * @return base64Content контент незашифрованного документа в кодировке BASE64
     */
    public String getBase64Content() {
        return base64Content;
    }

    /**
     * Устанавливает контент незашифрованного документа в кодировке BASE64
     * @param base64Content контент незашифрованного документа в кодировке BASE64
     */
    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    /**
     * Возвращает контент подписи в кодировке BASE64
     * @return signature контент подписи в кодировке BASE64
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Устанавливает контент подписи в кодировке BASE64
     * @param signature контент подписи в кодировке BASE64
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Возвращает дескриптор документа {@link DocumentDescription}
     * @return дескриптор документа
     */
    public DocumentDescription getDocumentDescription() {
        return documentDescription;
    }

    /**
     * Устанавливает дескриптор документа {@link DocumentDescription}
     * @param documentDescription дескриптор документа
     */
    public void setDocumentDescription(DocumentDescription documentDescription) {
        this.documentDescription = documentDescription;
    }
}
