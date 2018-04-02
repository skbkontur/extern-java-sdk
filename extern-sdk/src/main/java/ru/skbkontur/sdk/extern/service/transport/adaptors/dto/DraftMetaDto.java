/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.FnsRecipient;
import ru.skbkontur.sdk.extern.model.Organization;
import ru.skbkontur.sdk.extern.model.Recipient;

/**
 *
 * @author AlexS
 */
public class DraftMetaDto {

	public ru.skbkontur.sdk.extern.model.DraftMeta fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftMeta dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.DraftMeta draftMeta = new ru.skbkontur.sdk.extern.model.DraftMeta();

		ru.skbkontur.sdk.extern.service.transport.swagger.model.Sender dtoSender = dto.getSender();

		ru.skbkontur.sdk.extern.model.Sender sender = new ru.skbkontur.sdk.extern.model.Sender();
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

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftMeta toDto(ru.skbkontur.sdk.extern.model.DraftMeta draftMeta) {

		if (draftMeta == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftMeta dtoDraftMeta
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftMeta();

		ru.skbkontur.sdk.extern.service.transport.swagger.model.Sender dtoSender = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Sender();
		dtoSender.setInn(draftMeta.getSender().getInn());
		if (draftMeta.getSender().getKpp() != null && !draftMeta.getSender().getKpp().isEmpty()) {
			dtoSender.setKpp(draftMeta.getSender().getKpp());
		}

		ru.skbkontur.sdk.extern.service.transport.swagger.model.Certificate dtoCertificate = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Certificate();

		dtoSender.setCertificate(dtoCertificate.content(draftMeta.getSender().getCertificate()));

		dtoSender.setIpaddress(draftMeta.getSender().getIpaddress());

		dtoDraftMeta.setSender(dtoSender);

		dtoDraftMeta.setRecipient(createRecipentInfo(draftMeta.getRecipient()));

		ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountInfo dtoAccountInfo = new ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountInfo();
		dtoAccountInfo.setInn(draftMeta.getPayer().getInn());

		ru.skbkontur.sdk.extern.service.transport.swagger.model.OrganizationInfo dtoOrganizationInfo = new ru.skbkontur.sdk.extern.service.transport.swagger.model.OrganizationInfo();
		dtoOrganizationInfo.setKpp(draftMeta.getPayer().getKpp());
		dtoAccountInfo.setOrganization(dtoOrganizationInfo);

		dtoDraftMeta.setPayer(dtoAccountInfo);

		return dtoDraftMeta;
	}

	private ru.skbkontur.sdk.extern.service.transport.swagger.model.RecipientInfo createRecipentInfo(Recipient recipient) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.RecipientInfo recipientInfo = new ru.skbkontur.sdk.extern.service.transport.swagger.model.RecipientInfo();
		if (recipient instanceof FnsRecipient) {
			String fnsCode = ((FnsRecipient) recipient).getIfnsCode();
			recipientInfo.setIfnsCode(fnsCode);
			return recipientInfo;
		}
		return null;
	}

	private ru.skbkontur.sdk.extern.model.Recipient createRecipient(ru.skbkontur.sdk.extern.service.transport.swagger.model.RecipientInfo recipientDto) {
		ru.skbkontur.sdk.extern.model.Recipient recipient = null;
		if (recipientDto.getIfnsCode() != null && !recipientDto.getIfnsCode().isEmpty()) {
			recipient = new ru.skbkontur.sdk.extern.model.FnsRecipient(recipientDto.getIfnsCode());
		}
		return recipient;
	}
}
