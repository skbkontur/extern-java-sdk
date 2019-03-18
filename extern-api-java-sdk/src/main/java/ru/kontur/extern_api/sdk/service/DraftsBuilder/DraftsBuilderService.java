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

package ru.kontur.extern_api.sdk.service.DraftsBuilder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilder;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderMetaRequest;

public interface DraftsBuilderService<
        TDraftsBuilder extends DraftsBuilder,
        TDraftsBuilderMeta extends DraftsBuilderMeta,
        TDraftsBuilderMetaRequest extends DraftsBuilderMetaRequest> {
    /**
     * <p>POST /v1/{accountId}/drafts/builders</p>
     * Асинхронный метод создания билдера черновиков
     *
     * @param meta мета-данные билдера черновиков
     * @return билдер черновиков
     */
    CompletableFuture<TDraftsBuilder> createAsync(TDraftsBuilderMetaRequest meta);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}</p>
     * Асинхронный метод поиска билдера черновиков по идентификатору
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @return билдер черновиков
     */
    CompletableFuture<TDraftsBuilder> getAsync(UUID draftsBuilderId);

    /**
     * <p>DELETE /v1/{accountId}/drafts/builders/{draftsBuilderId}</p>
     * Асинхронный метод удаления билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @return {@link Void}
     */
    CompletableFuture deleteAsync(UUID draftsBuilderId);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/meta</p>
     * Асинхронный метод поиска мета-данных билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @return мета-данные билдера черновиков
     */
    CompletableFuture<TDraftsBuilderMeta> getMetaAsync(UUID draftsBuilderId);

    /**
     * <p>PUT /v1/{accountId}/drafts/builders/{draftsBuilderId}/meta</p>
     * Асинхронный метод обновления мета-данных билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param newMeta мета-данные билдера черновиков
     * @return мета-данные билдера черновиков
     */
    CompletableFuture<TDraftsBuilderMeta> updateMetaAsync(UUID draftsBuilderId, TDraftsBuilderMetaRequest newMeta);
}
