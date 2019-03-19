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

package ru.kontur.extern_api.sdk.model.DraftsBuilderModels;

public abstract class DraftsBuilderDocumentFileContents<TDraftsBuilderDocumentFileMetaRequest extends DraftsBuilderDocumentFileMetaRequest> {

    private String base64Content;
    private String base64SignatureContent;
    private TDraftsBuilderDocumentFileMetaRequest meta;

    /**
     * Возвращает контент файла
     *
     * @return контент файла
     */
    public String getBase64Content() {
        return base64Content;
    }

    /**
     * Устанавливает контент файла
     *
     * @param base64Content контент файла
     */
    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    /**
     * Возвращает контент подписи файла
     *
     * @return контент подписи файла
     */
    public String getBase64SignatureContent() {
        return base64SignatureContent;
    }

    /**
     * Устанавливает контент подписи файла
     *
     * @param base64SignatureContent контент подписи файла
     */
    public void setBase64SignatureContent(String base64SignatureContent) {
        this.base64SignatureContent = base64SignatureContent;
    }

    /**
     * Возвращает мета-данные файла
     *
     * @return мета-данные файла
     */
    public TDraftsBuilderDocumentFileMetaRequest getMeta() {
        return meta;
    }

    /**
     * Устанавливает мета-данные файла
     *
     * @param meta мета-данные файла
     */
    public void setMeta(TDraftsBuilderDocumentFileMetaRequest meta) {
        this.meta = meta;
    }
}
