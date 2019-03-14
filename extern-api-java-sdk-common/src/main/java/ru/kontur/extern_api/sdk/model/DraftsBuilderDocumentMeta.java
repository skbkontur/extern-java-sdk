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

package ru.kontur.extern_api.sdk.model;

public class DraftsBuilderDocumentMeta {
    public String builderType;
    public DraftsBuilderDocumentData builderData;

    /**
     * Возвращает тип билдера черновиков
     * @return тип билдера черновиков
     */
    public String getBuilderType() { return builderType; }

    /**
     * Устанавливает тип билдера черновиков
     * @param builderType тип билдера черновиков
     */
    public void setBuilderType(String builderType) { this.builderType = builderType; }

    /**
     * Возвращает объект {@link DraftsBuilderDocumentData}, содержащий дополнительные данные для указанного типа билдера черновиков
     * @return объект, содержащий дополнительные данные для указанного типа билдера черновиков
     * @see DraftsBuilderDocumentData
     */
    public DraftsBuilderDocumentData getBuilderData() { return builderData; }

    /**
     * Устанавливает объект {@link DraftsBuilderDocumentData}, содержащий дополнительные данные для указанного типа билдера черновиков
     * @param builderData объект {@link DraftsBuilderDocumentData}, содержащий дополнительные данные для указанного типа билдера черновиков
     * @see DraftsBuilderDocumentData
     */
    public void setBuilderData(DraftsBuilderDocumentData builderData) { this.builderData = builderData; }
}
