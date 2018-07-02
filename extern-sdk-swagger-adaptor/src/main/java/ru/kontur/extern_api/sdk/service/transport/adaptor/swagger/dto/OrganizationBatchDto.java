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

import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.model.CompanyBatch;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationBatchDto {

    public CompanyBatch fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationBatch dto) {
        if (dto == null) return null;

        CompanyBatch batch = new CompanyBatch();

        OrganizationDto organizationDto = new OrganizationDto();

        if (dto.getOrganizations() != null) {
            batch.setCompanies(
                dto.getOrganizations()
                    .stream()
                    .map(organizationDto::fromDto)
                    .collect(Collectors.toList())
            );
        }

        batch.setTotalCount(dto.getTotalCount());

        return batch;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationBatch toDto(CompanyBatch batch) {
        if (batch == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationBatch dto
            = new ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationBatch();

        OrganizationDto organizationDto = new OrganizationDto();

        if (batch.getCompanies() != null) {
            dto.setOrganizations(
                batch.getCompanies()
                    .stream()
                    .map(organizationDto::toDto)
                    .collect(Collectors.toList())
            );
        }

        batch.setTotalCount(dto.getTotalCount());

        return dto;
    }
}
