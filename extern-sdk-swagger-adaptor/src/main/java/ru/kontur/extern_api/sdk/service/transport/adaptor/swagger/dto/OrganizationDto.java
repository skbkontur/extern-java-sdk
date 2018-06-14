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
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import java.util.Map;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationGeneral;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationDto {

    public Company fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.Organization dto) {
        if (dto == null) return null;

        Company company = new Company();

        company.setId(dto.getId());
        if (dto.getGeneral() != null) {
            OrganizationGeneral g = dto.getGeneral();
            CompanyGeneral general = new CompanyGeneral();
            general.setInn(g.getInn());
            general.setKpp(g.getKpp());
            general.setName(g.getName());
            company.setGeneral(general);
        }

        return company;
    }

    public Company fromDto(Map<String,Object> dto) {
        if (dto == null) return null;

        Company company = new Company();

        company.setId((String)dto.get("id"));
        if (dto.get("general") != null) {
            Map<String,Object> g = (Map<String,Object>)dto.get("general");
            CompanyGeneral general = new CompanyGeneral();
            general.setInn((String)g.get("inn"));
            general.setKpp((String)g.get("kpp"));
            general.setName((String)g.get("name"));
            company.setGeneral(general);
        }

        return company;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.Organization toDto(Company company) {
        if (company == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Organization dto
            = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Organization();

        dto.setId(company.getId());
        if (company.getGeneral() != null) {
            CompanyGeneral g = company.getGeneral();
            ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationGeneral general
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationGeneral();
            general.setInn(g.getInn());
            general.setKpp(g.getKpp());
            general.setName(g.getName());
            dto.setGeneral(general);
        }

        return dto;
    }
}
