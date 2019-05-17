package ru.kontur.extern_api.sdk.model;

import ru.kontur.extern_api.sdk.model.ion.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.Ion1RequestData;
import ru.kontur.extern_api.sdk.model.ion.IonPeriod;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;

public class Ion1RequestContract extends IonRequestContract<Ion1RequestData> {
    public Ion1RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion1RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
