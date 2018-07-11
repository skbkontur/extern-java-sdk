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

/**
 * @author AlexS
 */
public class DraftDto {

    public ru.kontur.extern_api.sdk.model.Draft fromDto(
        ru.kontur.extern_api.sdk.service.transport.swagger.model.Draft dto) {

        if (dto == null) {
            return null;
        }

        ru.kontur.extern_api.sdk.model.Draft draft = new ru.kontur.extern_api.sdk.model.Draft();
        draft.setId(dto.getId());
        if (dto.getStatus() != null) {
            draft.setStatus(dto.getStatus().getValue());
        }

        if (dto.getMeta() != null) {
            draft.setMeta(new DraftMetaDto().fromDto(dto.getMeta()));
        }

        return draft;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.Draft toDto(
        ru.kontur.extern_api.sdk.model.Draft draft) {

        if (draft == null) {
            return null;
        }

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Draft dto
            = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Draft();
        dto.setId(draft.getId());
        ru.kontur.extern_api.sdk.service.transport.swagger.model.Draft.StatusEnum status
            = ru.kontur.extern_api.sdk.service.transport.swagger.model.Draft.StatusEnum
            .fromValue(draft.getStatus().getValue());
        dto.setStatus(status);

        return dto;
    }
}
