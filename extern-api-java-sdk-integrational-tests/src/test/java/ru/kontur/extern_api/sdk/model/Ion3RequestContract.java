package ru.kontur.extern_api.sdk.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.model.ion.*;
import ru.kontur.extern_api.sdk.model.ion.ClientInfo;

public class Ion3RequestContract extends IonRequestContract<Ion3RequestData> {
    public Ion3RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion3RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
