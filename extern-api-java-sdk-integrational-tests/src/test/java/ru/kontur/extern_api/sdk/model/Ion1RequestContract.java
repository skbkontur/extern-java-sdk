package ru.kontur.extern_api.sdk.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.model.ion.*;
import ru.kontur.extern_api.sdk.model.ion.ClientInfo;

public class Ion1RequestContract extends IonRequestContract<Ion1RequestData> {
    public Ion1RequestContract(@NotNull ClientInfo additionalOrgInfo, @Nullable IonPeriod period, @NotNull Ion1RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
