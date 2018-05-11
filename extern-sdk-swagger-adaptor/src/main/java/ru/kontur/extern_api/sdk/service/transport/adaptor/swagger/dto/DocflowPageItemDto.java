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

import ru.kontur.extern_api.sdk.model.DocflowPageItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import org.joda.time.DateTime;


/**
 * @author alexs
 */
class DocflowPageItemDto {

    public DocflowPageItem fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.DocflowPageItem dto) {

        if (dto == null) return null;

        DocflowPageItem docflowPageItem = new DocflowPageItem();

        LinkDto linkDto = new LinkDto();

        docflowPageItem.setId(dto.getId());
        docflowPageItem.setLastChangeDate(dto.getLastChangeDate()==null ? null :new Date(dto.getLastChangeDate().getMillis()));
        if (dto.getLinks() != null && !dto.getLinks().isEmpty()) {
            docflowPageItem.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        } else {
            docflowPageItem.setLinks(new ArrayList<>());
        }
        docflowPageItem.setSendDate(dto.getSendDate()==null ? null : new Date(dto.getSendDate().getMillis()));
        docflowPageItem.setStatus(dto.getStatus());
        docflowPageItem.setType(dto.getType());

        return docflowPageItem;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.DocflowPageItem toDto(DocflowPageItem docflowPageItem) {

        if (docflowPageItem == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.DocflowPageItem dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.DocflowPageItem();

        LinkDto linkDto = new LinkDto();

        dto.setId(docflowPageItem.getId());
        dto.setLastChangeDate(docflowPageItem.getLastChangeDate()==null ? null : new DateTime(docflowPageItem.getLastChangeDate().getTime()));
        dto.setLinks(docflowPageItem.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        dto.setSendDate(docflowPageItem.getSendDate()==null ? null : new DateTime(docflowPageItem.getSendDate().getTime()));
        dto.setStatus(docflowPageItem.getStatus());
        dto.setType(docflowPageItem.getType());

        return dto;
    }
}
