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

import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.TogsRecipient;


/**
 * @author AlexS
 */
public class DraftMetaDto {

    public ru.kontur.extern_api.sdk.model.DraftMeta fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftMeta dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.DraftMeta draftMeta = new ru.kontur.extern_api.sdk.model.DraftMeta();

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Sender dtoSender = dto.getSender();

        ru.kontur.extern_api.sdk.model.Sender sender = new ru.kontur.extern_api.sdk.model.Sender();
        sender.setInn(dtoSender.getInn());
        sender.setKpp(dtoSender.getKpp());
        sender.setCertificate(dtoSender.getCertificate() == null ? null : dtoSender.getCertificate().getContent());
        sender.setIpaddress(dto.getSender().getIpaddress());

        draftMeta.setSender(sender);

        draftMeta.setRecipient(createRecipient(dto.getRecipient()));

        draftMeta.setPayer(
                new Organization(dto.getPayer().getInn(), dto.getPayer().getOrganization() == null ? null : dto.getPayer().getOrganization().getKpp())
        );

        return draftMeta;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftMeta toDto(ru.kontur.extern_api.sdk.model.DraftMeta draftMeta) {

        if (draftMeta == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftMeta dtoDraftMeta
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftMeta();

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Sender dtoSender = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Sender();
        dtoSender.setInn(draftMeta.getSender().getInn());
        if (draftMeta.getSender().getKpp() != null && !draftMeta.getSender().getKpp().isEmpty()) {
            dtoSender.setKpp(draftMeta.getSender().getKpp());
        }

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Certificate dtoCertificate = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Certificate();

        dtoSender.setCertificate(dtoCertificate.content(draftMeta.getSender().getCertificate()));

        dtoSender.setIpaddress(draftMeta.getSender().getIpaddress());

        dtoDraftMeta.setSender(dtoSender);

        dtoDraftMeta.setRecipient(createRecipentInfo(draftMeta.getRecipient()));

        ru.kontur.extern_api.sdk.service.transport.swagger.model.AccountInfo dtoAccountInfo = new ru.kontur.extern_api.sdk.service.transport.swagger.model.AccountInfo();
        dtoAccountInfo.setInn(draftMeta.getPayer().getInn());

        ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationInfo dtoOrganizationInfo = new ru.kontur.extern_api.sdk.service.transport.swagger.model.OrganizationInfo();
        dtoOrganizationInfo.setKpp(draftMeta.getPayer().getKpp());
        dtoAccountInfo.setOrganization(dtoOrganizationInfo);

        dtoDraftMeta.setPayer(dtoAccountInfo);

        return dtoDraftMeta;
    }

    private ru.kontur.extern_api.sdk.service.transport.swagger.model.RecipientInfo createRecipentInfo(Recipient recipient) {
        ru.kontur.extern_api.sdk.service.transport.swagger.model.RecipientInfo recipientInfo = new ru.kontur.extern_api.sdk.service.transport.swagger.model.RecipientInfo();
        if (recipient instanceof FnsRecipient) {
            String fnsCode = ((FnsRecipient) recipient).getIfnsCode();
            recipientInfo.setIfnsCode(fnsCode);
            return recipientInfo;
        }
        else if (recipient instanceof TogsRecipient) {
            String togsCode = TogsRecipient.class.cast(recipient).getTogsCode();
            recipientInfo.setTogsCode(togsCode);
            return recipientInfo;
        }
        return null;
    }

    private ru.kontur.extern_api.sdk.model.Recipient createRecipient(ru.kontur.extern_api.sdk.service.transport.swagger.model.RecipientInfo recipientDto) {
        ru.kontur.extern_api.sdk.model.Recipient recipient = null;
        if (recipientDto.getIfnsCode() != null && !recipientDto.getIfnsCode().isEmpty()) {
            recipient = new ru.kontur.extern_api.sdk.model.FnsRecipient(recipientDto.getIfnsCode());
        }
        else if (recipientDto.getTogsCode() != null && !recipientDto.getTogsCode().isEmpty()) {
            recipient = new ru.kontur.extern_api.sdk.model.TogsRecipient(recipientDto.getTogsCode());
        }
        return recipient;
    }
}
