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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.model.CheckError;
import ru.kontur.extern_api.sdk.model.CheckResultData;


/**
 * @author AlexS
 */
public class CheckResultDataDto {

    public CheckResultData fromDto(
            ru.kontur.extern_api.sdk.service.transport.swagger.model.CheckResultData dto) {

        if (dto == null) {
            return null;
        }

        CheckResultData checkResultData = new CheckResultData();
        CheckErrorDto checkErrorDto = new CheckErrorDto();

        if (dto.getCommonErrors() != null) {
            checkResultData.setCommonErrors(
                    dto.getCommonErrors().stream().map(checkErrorDto::fromDto)
                            .collect(Collectors.toList()));
        }

        if (dto.getDocumentsErrors() != null) {
            checkResultData.setDocumentsErrors(
                    dto.getDocumentsErrors().entrySet().stream().collect(
                            Collectors.toMap(Map.Entry::getKey,
                                    l -> l.getValue().stream().map(checkErrorDto::fromDto)
                                            .collect(Collectors.toList()))
                    )
            );
        }
        return checkResultData;
    }

    public CheckResultData fromDto(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        CheckResultData checkResultData = new CheckResultData();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        if (data != null) {
            // collect documents-errors
            checkResultData.setDocumentsErrors(
                    (Map<String, List<CheckError>>) data.get("documents-errors"));
            // collect common-errors
            checkResultData.setCommonErrors(
                    (List<CheckError>) data.get("common-errors"));
        }
        System.out.println(checkResultData);
        return checkResultData;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.CheckResultData toDto(
            CheckResultData checkResultData) {

        if (checkResultData == null) {
            return null;
        }

        ru.kontur.extern_api.sdk.service.transport.swagger.model.CheckResultData dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.CheckResultData();

        CheckErrorDto checkErrorDto = new CheckErrorDto();

        if (checkResultData.getCommonErrors() != null) {
            dto.setCommonErrors(
                    checkResultData.getCommonErrors().stream().map(checkErrorDto::toDto)
                            .collect(Collectors.toList()));
        }

        if (checkResultData.getDocumentsErrors() != null) {
            dto.setDocumentsErrors(
                    checkResultData.getDocumentsErrors().entrySet().stream().collect(
                            Collectors.toMap(Map.Entry::getKey,
                                    l -> l.getValue().stream().map(checkErrorDto::toDto)
                                            .collect(Collectors.toList()))
                    )
            );
        }

        return dto;
    }
}
