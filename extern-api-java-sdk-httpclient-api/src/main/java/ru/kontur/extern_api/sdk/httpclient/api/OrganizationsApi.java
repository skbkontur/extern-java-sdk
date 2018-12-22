/*
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
 *
 */

package ru.kontur.extern_api.sdk.httpclient.api;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.model.CompanyName;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.OrganizationBatch;
import ru.kontur.extern_api.sdk.model.OrganizationGeneral;


@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface OrganizationsApi {

    @GET("v1/{accountId}/organizations/{orgId}")
    CompletableFuture<ApiResponse<Organization>> lookup(
            @Path("accountId") UUID accountId,
            @Path("orgId") UUID orgId
    );

    @GET("v1/{accountId}/organizations")
    CompletableFuture<ApiResponse<OrganizationBatch>> search(
            @Path("accountId") UUID accountId,
            @Query("skip") long skip,
            @Query("take") int take,
            @QueryMap Map<String, String> filters
    );

    @POST("v1/{accountId}/organizations")
    CompletableFuture<ApiResponse<Organization>> create(
            @Path("accountId") UUID accountId,
            @Body OrganizationGeneral companyGeneral
    );

    @PUT("v1/{accountId}/organizations/{orgId}")
    CompletableFuture<ApiResponse<Organization>> update(
            @Path("accountId") UUID accountId,
            @Path("orgId") UUID orgId,
            @Body CompanyName name
    );

    @DELETE("v1/{accountId}/organizations/{orgId}")
    CompletableFuture<ApiResponse<Void>> delete(
            @Path("accountId") UUID accountId,
            @Path("orgId") UUID orgId
    );
}
