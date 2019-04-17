package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.ClientInfo;
import ru.kontur.extern_api.sdk.model.ClientInfo.Sender;
import ru.kontur.extern_api.sdk.model.DemandRequestDto;
import ru.kontur.extern_api.sdk.model.DemandRequestOrganisationInfo;
import ru.kontur.extern_api.sdk.model.DemandRequestPayer;
import ru.kontur.extern_api.sdk.model.DemandRequestSender;
import ru.kontur.extern_api.sdk.model.Sender.Certificate;

public class DemandRequestDtoProvider {

    public static DemandRequestDto getDemandRequest(UUID accountId,
            ClientInfo clientInfo,
            String orgName,
            String[] knds) {
        return new DemandRequestDto(
                accountId,
                getDemandRequestSender(clientInfo, orgName),
                getDemandRequestPayer(clientInfo, orgName),
                knds);
    }

    private static DemandRequestPayer getDemandRequestPayer(ClientInfo clientInfo, String orgName) {
        DemandRequestPayer demandRequestPayer = new DemandRequestPayer();
        demandRequestPayer.setInn(clientInfo.getOrganization().getInn());
        demandRequestPayer
                .setOrganisationInfo(new DemandRequestOrganisationInfo(clientInfo.getOrganization().getKpp()));
        demandRequestPayer.setName(orgName);
        return demandRequestPayer;
    }


    private static DemandRequestSender getDemandRequestSender(ClientInfo clientInfo, String ogrName) {
        DemandRequestSender demandRequestSender = new DemandRequestSender();
        Sender sender = clientInfo.getSender();
        demandRequestSender.setInn(sender.getInn());
        demandRequestSender.setKpp(sender.getKpp());
        demandRequestSender.setName(ogrName);
        demandRequestSender.setCertificate(new Certificate(sender.getCertificate()));
        return demandRequestSender;
    }
}
