package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

public class SessionSignDraftData {

    /**
     * Идентификатор криптосессии
     */
    private UUID cryptoSessionId;


    public UUID getId() {
        return cryptoSessionId;
    }


    public void setId(UUID id) {
        this.cryptoSessionId = cryptoSessionId;
    }
}
