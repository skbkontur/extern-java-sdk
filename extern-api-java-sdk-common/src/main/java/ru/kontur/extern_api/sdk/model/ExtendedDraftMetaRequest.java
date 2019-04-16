package ru.kontur.extern_api.sdk.model;

public class ExtendedDraftMetaRequest extends DraftMetaRequest {
    private AdditionalInfoRequest additionalInfo;

    public ExtendedDraftMetaRequest(){

    }

    public ExtendedDraftMetaRequest(SenderRequest sender, Recipient recipient, OrganizationRequest payer){
        super(sender, recipient, payer);
        additionalInfo = new AdditionalInfoRequest();
    }

    public ExtendedDraftMetaRequest(SenderRequest sender, Recipient recipient, OrganizationRequest payer, String subject){
        super(sender, recipient, payer);
        additionalInfo = new AdditionalInfoRequest(subject);
    }

    public ExtendedDraftMetaRequest(DraftMetaRequest draftMeta, String subject){
        setPayer(draftMeta.getPayer());
        setRecipient(draftMeta.getRecipient());
        setSender(draftMeta.getSender());
        additionalInfo = new AdditionalInfoRequest(subject);
    }

    public AdditionalInfoRequest getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfoRequest additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
