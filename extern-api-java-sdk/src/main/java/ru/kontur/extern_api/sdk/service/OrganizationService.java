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

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

/**
 * Группа методов предоставляет доступ к операциям для работы с организациями:
 * <p>- найти организацию по ее внутреннему идентификатору {@link OrganizationService#lookupAsync} | {@link OrganizationService#lookup};</p>
 * <p>- создать организацию {@link OrganizationService#createAsync} | {@link OrganizationService#create};</p>
 * <p>- изменить наименования организации {@link OrganizationService#updateAsync} | {@link OrganizationService#update};</p>
 */
public interface OrganizationService extends ProviderHolder {

    /**
     * <p>GET /v1/{accountId}/organizations/{orgId}</p>
     * Асинхронный метод возвращает организацию по ее внутреннему идентификатору
     * @param companyId идентификатор организации
     * @return организация
     * @see Company
     */
    CompletableFuture<QueryContext<Company>> lookupAsync(String companyId);

    /**
     * <p>GET /v1/{accountId}/organizations/{orgId}</p>
     * Синхронный метод возвращает организацию по ее внутреннему идентификатору
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>-  идентификатор организации. Для установки необходимо использовать метод {@link QueryContext#setCompanyId};</p>
     * @return организация
     * @see Company
     */
    QueryContext<Company> lookup(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/organizations</p>
     * Асинхронный метод создает новую организацию
     * @param companyGeneral данные новой организации {@link CompanyGeneral}
     * @return организация
     * @see Company
     */
    CompletableFuture<QueryContext<Company>> createAsync(CompanyGeneral companyGeneral);

    /**
     * <p>POST /v1/{accountId}/organizations</p>
     * Асинхронный метод создает новую организацию
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>-  данные новой организации {@link CompanyGeneral}. Для установки необходимо использовать метод {@link QueryContext#setCompanyGeneral};</p>
     * @return организация
     * @see Company
     */
    QueryContext<Company> create(QueryContext<?> cxt);

    /**
     * <p>PUT /v1/{accountId}/organizations/{orgId}</p>
     * Асинхронный метод для обновления наименования организации
     * @param companyId идентификатор организации
     * @param name наименование организации
     * @return организация
     * @see Company
     */
    CompletableFuture<QueryContext<Company>> updateAsync(String companyId, String name);

    /**
     * <p>PUT /v1/{accountId}/organizations/{orgId}</p>
     * Синхронный метод для обновления наименования организации
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>-  идентификатор организации. Для установки необходимо использовать метод {@link QueryContext#setCompanyId}.</p>
     * @return организация
     * @see Company
     */
    QueryContext<Company> update(QueryContext<?> cxt);

    /**
     * <p>DELETE /v1/{accountId}/organizations/{orgId}</p>
     * Cинхронный метод для удаления организации
     * @param companyId идентификатор организации
     * @return Void
     */
    CompletableFuture<QueryContext<Void>> deleteAsync(String companyId);

    /**
     * <p>DELETE /v1/{accountId}/organizations/{orgId}</p>
     * Cинхронный метод для удаления организации
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>-  идентификатор организации. Для установки необходимо использовать метод {@link QueryContext#setCompanyId}.</p>
     * @return Void
     */
    QueryContext<Void> delete(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/organizations</p>
     * Асинхронный метод поиска организаций по ИНН и КПП. Возвращает постранично список организаций.
     * @param inn инн
     * @param kpp кпп
     * @param skip смещение от начала списка
     * @param take максимальное количество организаций в возвращаемом списке
     * @return список организаций
     * @see CompanyBatch
     */
    CompletableFuture<QueryContext<CompanyBatch>> searchAsync(
            String inn,
            String kpp,
            Long skip,
            Integer take);
    /**
     * <p>GET /v1/{accountId}/organizations</p>
     * Синхронный метод поиска организаций по ИНН и КПП. Возвращает постранично список организаций.
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>-  ИНН. Для установки необходимо использовать метод {@link QueryContext#setInn};</p>
     * <p>-  КПП. Для установки необходимо использовать метод {@link QueryContext#setKpp};</p>
     * <p>-  смещение от начала списка. Для установки необходимо использовать метод {@link QueryContext#setSkip};</p>
     * <p>-  максимальное количество организаций в возвращаемом списке. Для установки необходимо использовать метод {@link QueryContext#setTake}.</p>
     * @return список организаций
     * @see CompanyBatch
     */
    QueryContext<CompanyBatch> search(QueryContext<?> cxt);
}
