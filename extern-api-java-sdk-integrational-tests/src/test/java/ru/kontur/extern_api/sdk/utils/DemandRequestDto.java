package ru.kontur.extern_api.sdk.utils;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.ClientInfo;

public class DemandRequestDto {

    public DemandRequestDto(UUID accountId, ClientInfo clientInfo, String orgName, String[] knds) {
        this.accountId = accountId;
        this.sender = DemandRequestSender.fromClientInfo(clientInfo, orgName);
        this.payer = DemandRequestPayer.fromClientInfo(clientInfo, orgName);
        this.knds = knds;
    }

    public DemandRequestSender getDemandRequestSender() {
        return sender;
    }

    public void setDemandRequestSender(DemandRequestSender demandRequestSender) {
        this.sender = demandRequestSender;
    }

    public String[] getDocflowCount() {
        return knds;
    }

    public void setDocflowCount(String[] knds) {
        this.knds = knds;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String[] getKnds() {
        return knds;
    }

    public void setKnds(String[] knds) {
        this.knds = knds;
    }

    private UUID accountId;

    private DemandRequestSender sender;

    public void setPayer(DemandRequestPayer payer) {
        this.payer = payer;
    }

    private DemandRequestPayer payer;
    private String[] knds;
}
