package ru.kontur.extern_api.sdk.model;

import ru.kontur.extern_api.sdk.model.ion.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.Ion4RequestData;
import ru.kontur.extern_api.sdk.model.ion.IonPeriod;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;

public class Ion4RequestContract extends IonRequestContract<Ion4RequestData> {
    public Ion4RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion4RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
