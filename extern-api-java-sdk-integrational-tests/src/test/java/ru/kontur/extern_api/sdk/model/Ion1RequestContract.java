package ru.kontur.extern_api.sdk.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.model.ion.*;
import ru.kontur.extern_api.sdk.model.ion.ClientInfo;

public class Ion1RequestContract extends IonRequestContract<Ion1RequestData> {
    public Ion1RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion1RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
