/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.OrganizationBatch;
import ru.kontur.extern_api.sdk.model.OrganizationGeneral;

/**
 * Группа методов предоставляет доступ к операциям для работы с организациями:
 * <p>- найти организацию по ее внутреннему идентификатору {@link OrganizationService#lookupAsync} | {@link
 * OrganizationService#lookup};</p>
 * <p>- создать организацию {@link OrganizationService#createAsync} | {@link OrganizationService#create};</p>
 * <p>- изменить наименования организации {@link OrganizationService#updateAsync} | {@link
 * OrganizationService#update};</p>
 */
public interface OrganizationService {

    /**
     * <p>GET /v1/{accountId}/organizations/{orgId}</p>
     * Асинхронный метод возвращает организацию по ее внутреннему идентификатору
     *
     * @param companyId идентификатор организации
     * @return организация
     * @see ru.kontur.extern_api.sdk.model.Organization
     */
    CompletableFuture<QueryContext<Organization>> lookupAsync(UUID companyId);

    /**
     * <p>GET /v1/{accountId}/organizations/{orgId}</p>
     * Асинхронный метод возвращает организацию по ее внутреннему идентификатору
     *
     * @param companyId идентификатор организации
     * @return организация
     * @see Organization
     */
    CompletableFuture<QueryContext<Organization>> lookupAsync(String companyId);

    /**
     * <p>GET /v1/{accountId}/organizations/{orgId}</p>
     * Синхронный метод возвращает организацию по ее внутреннему идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>-  идентификатор организации. Для установки необходимо использовать метод {@link
     *         QueryContext#setCompanyId};</p>
     * @return организация
     * @see Organization
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Organization> lookup(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/organizations</p>
     * Асинхронный метод создает новую организацию
     *
     * @param companyGeneral данные новой организации {@link ru.kontur.extern_api.sdk.model.OrganizationGeneral}
     * @return организация
     * @see Organization
     */
    CompletableFuture<QueryContext<Organization>> createAsync(OrganizationGeneral companyGeneral);

    /**
     * <p>POST /v1/{accountId}/organizations</p>
     * Асинхронный метод создает новую организацию
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>-  данные новой организации {@link OrganizationGeneral}. Для установки необходимо использовать метод {@link
     *         QueryContext#setCompanyGeneral};</p>
     * @return организация
     * @see  Organization
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Organization> create(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Organization>> updateAsync(UUID companyId, String name);

    /**
     * <p>PUT /v1/{accountId}/organizations/{orgId}</p>
     * Асинхронный метод для обновления наименования организации
     *
     * @param companyId идентификатор организации
     * @param name наименование организации
     * @return организация
     * @see Organization
     */
    CompletableFuture<QueryContext<Organization>> updateAsync(String companyId, String name);

    /**
     * <p>PUT /v1/{accountId}/organizations/{orgId}</p>
     * Синхронный метод для обновления наименования организации
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>-  идентификатор организации. Для установки необходимо использовать метод {@link
     *         QueryContext#setCompanyId}.</p>
     * @return организация
     * @see Organization
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Organization> update(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> deleteAsync(UUID companyId);

    /**
     * <p>DELETE /v1/{accountId}/organizations/{orgId}</p>
     * Cинхронный метод для удаления организации
     *
     * @param companyId идентификатор организации
     * @return Void
     */
    CompletableFuture<QueryContext<Void>> deleteAsync(String companyId);

    /**
     * <p>DELETE /v1/{accountId}/organizations/{orgId}</p>
     * Cинхронный метод для удаления организации
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>-  идентификатор организации. Для установки необходимо использовать метод {@link
     *         QueryContext#setCompanyId}.</p>
     * @return Void
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Void> delete(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/organizations</p>
     * Асинхронный метод поиска организаций по ИНН и КПП. Возвращает постранично список организаций.
     *
     * @param filter параметры поиска
     * @return список организаций
     * @see OrganizationBatch
     */
    CompletableFuture<QueryContext<OrganizationBatch>> searchAsync(OrgFilter filter);

    /**
     * <p>GET /v1/{accountId}/organizations</p>
     * Асинхронный метод поиска организаций по ИНН и КПП. Возвращает постранично список организаций.
     *
     * @param inn ИНН
     * @param kpp КПП
     * @param skip смещение от начала списка
     * @param take максимальное количество организаций в возвращаемом списке
     * @return список организаций
     * @see OrganizationBatch
     * @deprecated use {@link OrganizationService#searchAsync(OrgFilter)} instead
     */
    @Deprecated
    CompletableFuture<QueryContext<OrganizationBatch>> searchAsync(
            @Nullable String inn,
            @Nullable String kpp,
            @Nullable Integer skip,
            @Nullable Integer take
    );

    /**
     * <p>GET /v1/{accountId}/organizations</p>
     * Синхронный метод поиска организаций по ИНН и КПП. Возвращает постранично список организаций.
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>-  ИНН. Для установки необходимо использовать метод {@link QueryContext#setInn};</p>
     *         <p>-  КПП. Для установки необходимо использовать метод {@link QueryContext#setKpp};</p>
     *         <p>-  смещение от начала списка. Для установки необходимо использовать метод {@link
     *         QueryContext#setSkip};</p>
     *         <p>-  максимальное количество организаций в возвращаемом списке. Для установки необходимо использовать
     *         метод {@link QueryContext#setTake}.</p>
     * @return список организаций
     * @see OrganizationBatch
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<OrganizationBatch> search(QueryContext<?> cxt);
}
