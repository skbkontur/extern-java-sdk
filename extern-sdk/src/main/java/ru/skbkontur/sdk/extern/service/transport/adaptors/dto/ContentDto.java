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

/**
 * @author AlexS
 */
class ContentDto {

    public ru.skbkontur.sdk.extern.model.Content fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Content dto) {

        if (dto == null) return null;

        LinkDto linkDto = new LinkDto();
        ru.skbkontur.sdk.extern.model.Content content = new ru.skbkontur.sdk.extern.model.Content();
        content.setDecrypted(linkDto.fromDto(dto.getDecrypted()));
        content.setEncrypted(linkDto.fromDto(dto.getEncrypted()));

        return content;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.Content toDto(ru.skbkontur.sdk.extern.model.Content content) {

        if (content == null) return null;

        LinkDto linkDto = new LinkDto();
        ru.skbkontur.sdk.extern.service.transport.swagger.model.Content dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Content();
        dto.setDecrypted(linkDto.toDto(content.getDecrypted()));
        dto.setEncrypted(linkDto.toDto(content.getEncrypted()));

        return dto;
    }

}
