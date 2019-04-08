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

package ru.kontur.extern_api.sdk.model.builders;

import java.util.UUID;

public abstract class DraftsBuilder<TDraftsBuilderMeta extends DraftsBuilderMeta> {

    private UUID id;
    private TDraftsBuilderMeta meta;
    private DraftsBuilderStatus status;

    /**
     * Возвращает идентификатор билдера черновиков
     *
     * @return идентификатор билдера черновиков
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор билдера черновиков
     *
     * @param id идентификатор билдера черновиков
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Возвращает мета-данные билдера черновиков
     *
     * @return мета-данные билдера черновиков
     * @see DraftsBuilderMeta
     */
    public TDraftsBuilderMeta getMeta() {
        return meta;
    }

    /**
     * Устанавливает мета-данные билдера черновиков
     *
     * @param draftsBuilderMeta мета-данные билдера черновиков
     */
    public void setMeta(TDraftsBuilderMeta draftsBuilderMeta) {
        meta = draftsBuilderMeta;
    }

    /**
     * Возвращает статус билдера черновиков
     *
     * @return статус билдера черновиков
     * @see DraftsBuilderStatus
     */
    public DraftsBuilderStatus getStatus() {
        return status;
    }

    /**
     * Устанавливает статус билдера черновиков
     *
     * @param draftsBuilderStatus статус билдера черновиков
     */
    public void setStatus(DraftsBuilderStatus draftsBuilderStatus) {
        status = draftsBuilderStatus;
    }

    private enum DraftsBuilderStatus {
        New,
        Building,
        Finished
    }
}

