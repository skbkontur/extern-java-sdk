package ru.kontur.extern_api.sdk.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.model.ion.*;
import ru.kontur.extern_api.sdk.model.ion.ClientInfo;

public class Ion4RequestContract extends IonRequestContract<Ion4RequestData> {
    public Ion4RequestContract(@NotNull ClientInfo additionalOrgInfo, @Nullable IonPeriod period, @NotNull Ion4RequestData data) {
        super(additionalOrgInfo, period, data);
    }
}
