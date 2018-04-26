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

package ru.kontur.extern_api.sdk.service.transport.adaptors.dto;

import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class PrepareResultDto {

    public ru.kontur.extern_api.sdk.model.PrepareResult fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.PrepareResult dto) {

        if (dto == null) return null;

        CheckResultDataDto checkResultDataDto = new CheckResultDataDto();
        LinkDto linkDto = new LinkDto();
        ru.kontur.extern_api.sdk.model.PrepareResult prepareResult = new ru.kontur.extern_api.sdk.model.PrepareResult();
        prepareResult.setCheckResult(checkResultDataDto.fromDto(dto.getCheckResult()));
        prepareResult.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        prepareResult.setStatus(ru.kontur.extern_api.sdk.model.PrepareResult.Status.fromValue(dto.getStatus().getValue()));

        return prepareResult;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.PrepareResult toDto(ru.kontur.extern_api.sdk.model.PrepareResult prepareResult) {

        if (prepareResult == null) return null;

        CheckResultDataDto checkResultDataDto = new CheckResultDataDto();
        LinkDto linkDto = new LinkDto();
        ru.kontur.extern_api.sdk.service.transport.swagger.model.PrepareResult dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.PrepareResult();
        dto.setCheckResult(checkResultDataDto.toDto(prepareResult.getCheckResult()));
        dto.setLinks(prepareResult.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        dto.setStatus(ru.kontur.extern_api.sdk.service.transport.swagger.model.PrepareResult.StatusEnum.fromValue(prepareResult.getStatus().getValue()));

        return dto;
    }
}
