package ru.kontur.extern_api.sdk.model;

import ru.kontur.extern_api.sdk.model.ion.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.Ion3RequestData;
import ru.kontur.extern_api.sdk.model.ion.IonPeriod;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;

public class Ion3RequestContract extends IonRequestContract<Ion3RequestData> {
    public Ion3RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion3RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
