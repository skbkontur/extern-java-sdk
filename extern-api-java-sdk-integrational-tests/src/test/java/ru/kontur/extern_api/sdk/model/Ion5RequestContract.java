package ru.kontur.extern_api.sdk.model;

import ru.kontur.extern_api.sdk.model.ion.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.Ion5RequestData;
import ru.kontur.extern_api.sdk.model.ion.IonPeriod;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;

public class Ion5RequestContract extends IonRequestContract<Ion5RequestData> {
    public Ion5RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion5RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
