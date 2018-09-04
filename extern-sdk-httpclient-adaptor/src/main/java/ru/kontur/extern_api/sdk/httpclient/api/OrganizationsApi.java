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

import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationsApi extends RestApi {

    @Path("/v1/{accountId}/organizations/{orgId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Company> lookup(@PathParam("accountId") final String accountId,@PathParam("orgId") final String orgId) throws ApiException {
        return invoke("lookup", null, new TypeToken<Company>(){}.getType(), accountId, orgId);
    }

    @Path("/v1/{accountId}/organizations")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Company> create(@PathParam("accountId") final String accountId, final CompanyGeneral companyGeneral) throws ApiException {
        return invoke("create", companyGeneral, new TypeToken<Company>(){}.getType(), accountId);
    }

    @Path("/v1/{accountId}/organizations/{orgId}")
    @PUT
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Company> update(@PathParam("accountId") final String accountId, @PathParam("orgId") final String orgId, final String name) throws ApiException {
        Map<String,Object> updateOrganizationRequestDto = new HashMap<>();

        updateOrganizationRequestDto.put("name",name);

        return invoke("update", updateOrganizationRequestDto, new TypeToken<Company>(){}.getType(), accountId, orgId);
    }

    @Path("/v1/{accountId}/organizations/{orgId}")
    @DELETE
    public ApiResponse<Void> delete(@PathParam("accountId") final String accountId, @PathParam("orgId") final String orgId) throws ApiException {
        return invoke("delete", null, new TypeToken<Void>(){}.getType(), accountId, orgId);
    }

    @Path("/v1/{accountId}/organizations")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<CompanyBatch> search(
        @PathParam("accountId") final String accountId,
        @QueryParam("inn") final String inn,
        @QueryParam("kpp") final String kpp,
        @QueryParam("skip") final Long skip,
        @QueryParam("take") final Integer take
    ) throws ApiException {
        return invoke("search", null, new TypeToken<CompanyBatch>(){}.getType(), accountId, inn, kpp, skip, take);
    }
}
