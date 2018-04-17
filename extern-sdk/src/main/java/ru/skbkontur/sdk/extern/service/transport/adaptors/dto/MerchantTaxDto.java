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

import ru.skbkontur.sdk.extern.model.MerchantTax;


/**
 * @author alexs
 */
public class MerchantTaxDto {

    public MerchantTax fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax dto) {

        if (dto == null) return null;

        MerchantTax t = new MerchantTax();

        t.setDohod(dto.getDohod());
        t.setIschisl(dto.getIschisl());
        t.setTorgSborFact(dto.getTorgSborFact());
        t.setTorgSborUmen(dto.getTorgSborUmen());

        return t;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax toDto(MerchantTax t) {

        if (t == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax();

        dto.setDohod(t.getDohod());
        dto.setIschisl(t.getIschisl());
        dto.setTorgSborFact(t.getTorgSborFact());
        dto.setTorgSborUmen(t.getTorgSborUmen());

        return dto;
    }
}
