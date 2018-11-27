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

package ru.kontur.extern_api.sdk.utils;

import com.google.gson.Gson;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.ServiceException;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.service.OrganizationService;

public class OrganizationServiceUtils {

    private static Logger log = Logger.getLogger(OrganizationServiceUtils.class.getName());

    private static Gson gson = GsonProvider
            .getPreConfiguredGsonBuilder()
            .setPrettyPrinting()
            .create();

    private final OrganizationService service;

    private final String accountId;

    public OrganizationServiceUtils(
            @NotNull OrganizationService service,
            @NotNull String accountId) {
        this.service = service;
        this.accountId = accountId;
    }

    /**
     * Creates an Organization if Organization with given inn & kpp does not exist
     *
     * @throws ServiceException when something goes wrong
     * @returns registered or existed Company.
     */
    public Company createIfNotExist(String inn, String kpp)
            throws ExecutionException, InterruptedException {

        CompanyBatch orgs = service
                .searchAsync(OrgFilter.page(0, 1).inn(inn).kpp(kpp))
                .get()
                .getOrThrow();

        if (orgs.getTotalCount() > 0) {
            Company org = orgs.getCompanies().get(0);
            log.info("Found existing organization: " + gson.toJson(org));
            return org;
        }

        CompanyGeneral companyGeneral = new CompanyGeneral();

        companyGeneral.setName("Teremok #" + inn);
        companyGeneral.setInn(inn);
        companyGeneral.setKpp(kpp);

        log.info("Creating organization: " + gson.toJson(companyGeneral));
        return service
                .createAsync(companyGeneral)
                .get()
                .getOrThrow();
    }

    public OrganizationService getService() {
        return service;
    }

    public String getTargetAccount() {
        return accountId;
    }
}
