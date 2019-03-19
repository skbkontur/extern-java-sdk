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

import java.util.UUID;

public abstract class DraftsBuilderDocument<TMeta extends DraftsBuilderDocumentMeta> {

    public UUID id;
    public UUID draftsBuilderId;
    public TMeta meta;

    /**
     * Возвращает идентификатор документа билдера черновиков
     *
     * @return идентификатор документа билдера черновиков
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор файла документа билдера черновиков
     *
     * @param id идентификатор файла документа билдера черновиков
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор билдера черновиков
     *
     * @return идентификатор билдера черновиков
     */
    public UUID getDraftsBuilderId() {
        return draftsBuilderId;
    }

    /**
     * Устанавливает идентификатор билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     */
    public void setDraftsBuilderId(UUID draftsBuilderId) {
        this.draftsBuilderId = draftsBuilderId;
    }

    /**
     * Возвращает мета-данные документа билдера черновиков
     *
     * @return мета-данные документа билдера черновиков
     */
    public TMeta getMeta() {
        return meta;
    }

    /**
     * Устанавливает мета-данные документа билдера черновиков
     *
     * @param meta мета-данные документа билдера черновиков
     */
    public void setMeta(TMeta meta) {
        this.meta = meta;
    }

}
