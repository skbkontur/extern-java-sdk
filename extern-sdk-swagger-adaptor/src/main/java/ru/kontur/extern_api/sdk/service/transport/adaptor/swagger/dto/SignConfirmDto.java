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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import java.util.function.Function;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.Link;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.SignConfirmResult;

public class SignConfirmDto {

    public SignedDraft fromDto(SignConfirmResult signConfirmResult) {
        SignedDraft model = new SignedDraft();

        Function<Link, ru.kontur.extern_api.sdk.model.Link> fromDto = new LinkDto()::fromDto;
        model.setSignedDocuments(ListUtils.map(fromDto, signConfirmResult.getSignedDocuments()));

        return model;
    }

    public SignConfirmResult toDto(SignedDraft signedDraft) {
        SignConfirmResult dto = new SignConfirmResult();

        Function<ru.kontur.extern_api.sdk.model.Link, Link> toDto = new LinkDto()::toDto;
        dto.setSignedDocuments(ListUtils.map(toDto, signedDraft.getSignedDocuments()));

        return dto;
    }


}
