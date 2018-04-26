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

import org.joda.time.DateTime;
import ru.kontur.extern_api.sdk.model.ApiEvent;

import java.util.Date;


/**
 * @author AlexS
 */
class ApiEventDto {

    public ApiEvent fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.ApiEvent dto) {
        if (dto == null) return null;

        ApiEvent ae = new ApiEvent();

        ae.setDocflowLink(new LinkDto().fromDto(dto.getDocflowLink()));
        ae.setDocflowType(dto.getDocflowType());
        ae.setEventDateTime(new Date(dto.getEventDateTime().getMillis()));
        ae.setId(dto.getId());
        ae.setInn(dto.getInn());
        ae.setKpp(dto.getKpp());
        ae.setNewState(dto.getNewState());

        return ae;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.ApiEvent toDto(ApiEvent ae) {
        if (ae == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.ApiEvent dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.ApiEvent();

        dto.setDocflowLink(new LinkDto().toDto(ae.getDocflowLink()));
        dto.setDocflowType(ae.getDocflowType());
        dto.setEventDateTime(new DateTime(ae.getEventDateTime().getTime()));
        dto.setId(ae.getId());
        dto.setInn(ae.getInn());
        dto.setKpp(ae.getKpp());
        dto.setNewState(ae.getNewState());

        return dto;
    }
}
