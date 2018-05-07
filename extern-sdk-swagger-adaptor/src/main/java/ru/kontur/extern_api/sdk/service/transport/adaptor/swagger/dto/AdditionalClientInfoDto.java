/*
 * MIT License
 *
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
 */

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import ru.kontur.extern_api.sdk.model.AdditionalClientInfo;


/**
 * @author alexs
 */
class AdditionalClientInfoDto {

    public AdditionalClientInfo fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.AdditionalClientInfo dto) {

        if (dto == null) return null;

        AdditionalClientInfo info = new AdditionalClientInfo();

        TaxpayerDto taxpayerDto = new TaxpayerDto();

        info.setSenderFullName(dto.getDocumentSender().getSenderFullName());
        info.setSignerType(AdditionalClientInfo.SignerTypeEnum.fromValue(dto.getSignerType().getValue()));
        info.setTaxpayer(taxpayerDto.fromDto(dto.getTaxpayer()));

        return info;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.AdditionalClientInfo toDto(AdditionalClientInfo info) {

        if (info == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.AdditionalClientInfo dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.AdditionalClientInfo();

        TaxpayerDto taxpayerDto = new TaxpayerDto();

        ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentSender sender
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentSender();

        sender.setSenderFullName(info.getSenderFullName());

        dto.setDocumentSender(sender);
        dto.setSignerType(ru.kontur.extern_api.sdk.service.transport.swagger.model.AdditionalClientInfo.SignerTypeEnum.fromValue(info.getSignerType().getValue()));
        dto.setTaxpayer(taxpayerDto.toDto(info.getTaxpayer()));

        return dto;
    }
}
