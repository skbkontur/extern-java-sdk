package ru.kontur.extern_api.sdk.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.model.ion.*;
import ru.kontur.extern_api.sdk.model.ion.ClientInfo;

public class Ion2RequestContract extends IonRequestContract<Ion2RequestData> {
    public Ion2RequestContract(ClientInfo additionalOrgInfo, IonPeriod period, Ion2RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
