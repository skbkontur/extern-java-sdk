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

package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.DocflowPage;

import java.util.stream.Collectors;


/**
 * @author alexs
 */
public class DocflowPageDto {

    public DocflowPage fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage dto) {

        if (dto == null) return null;

        DocflowPage docflowPage = new DocflowPage();

        DocflowPageItemDto docflowPageItemDto = new DocflowPageItemDto();

        docflowPage.setDocflowsPageItem(dto.getDocflowsPageItem().stream().map(docflowPageItemDto::fromDto).collect(Collectors.toList()));
        docflowPage.setSkip(dto.getSkip());
        docflowPage.setTake(dto.getTake());
        docflowPage.setTotalCount(dto.getTotalCount());

        return docflowPage;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage toDto(DocflowPage docflowPage) {

        if (docflowPage == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage();

        DocflowPageItemDto docflowPageItemDto = new DocflowPageItemDto();

        dto.setDocflowsPageItem(docflowPage.getDocflowsPageItem().stream().map(docflowPageItemDto::toDto).collect(Collectors.toList()));
        dto.setSkip(docflowPage.getSkip());
        dto.setTake(docflowPage.getTake());
        dto.setTotalCount(docflowPage.getTotalCount());

        return dto;
    }
}
