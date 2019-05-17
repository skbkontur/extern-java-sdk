package ru.kontur.extern_api.sdk.model;

import ru.kontur.extern_api.sdk.model.ion.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.Ion2RequestData;
import ru.kontur.extern_api.sdk.model.ion.IonPeriod;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;

public class Ion2RequestContract extends IonRequestContract<Ion2RequestData> {
    public Ion2RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion2RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
