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

import java.util.Map;


/**
 * @author AlexS
 */
class SignatureToSendDto {

    public SignatureToSendDto() {
    }

    public ru.kontur.extern_api.sdk.model.SignatureToSend fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.SignatureToSend signatureToSend = new ru.kontur.extern_api.sdk.model.SignatureToSend();

        signatureToSend.setContentData(dto.getContentData());
        signatureToSend.setId(dto.getId());

        return signatureToSend;
    }

    public ru.kontur.extern_api.sdk.model.SignatureToSend fromDto(Map<String, Object> dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.SignatureToSend signatureToSend = new ru.kontur.extern_api.sdk.model.SignatureToSend();

        signatureToSend.setContentData((byte[]) dto.get("content-data"));
        signatureToSend.setId((String) dto.get("id"));

        return signatureToSend;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend toDto(ru.kontur.extern_api.sdk.model.SignatureToSend signatureToSend) {

        if (signatureToSend == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend();

        dto.setContentData(signatureToSend.getContentData());
        dto.setId(signatureToSend.getId());

        return dto;
    }
}
