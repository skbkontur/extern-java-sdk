package ru.kontur.extern_api.sdk.utils;

import ru.kontur.extern_api.sdk.model.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;
import ru.kontur.extern_api.sdk.model.ion.IonRequestData;

interface IIonBuilder {

    interface Syntax<T extends IonRequestData> extends
            SetSyntax<T>,
            SetIonDataSyntax<T>,
            BuildSyntax<T> {
    }

    interface SetSyntax<T extends IonRequestData> {
        SetIonDataSyntax set(ClientInfo clientInfo);
    }

    interface SetIonDataSyntax<T extends IonRequestData> {
        BuildSyntax<T> setIonData(T ionData);
    }

    interface BuildSyntax<T extends IonRequestData> {
        IonRequestContract<T> build();
    }
}

public class IonBuilder<T extends IonRequestData> implements IIonBuilder.Syntax<T> {

    private IonBuilder(int ionVersion) {

    }

    public static <T extends IonRequestData> IIonBuilder.SetSyntax<T> Create(int ionVersion) {
        return new IonBuilder<>(ionVersion);
    }

    public static IIonBuilder.SetSyntax<IonRequestData> CreateIonRequestData(int ionVersion) {
        return new IonBuilder<>(ionVersion);
    }

    // todo: Implement methods, Get rid of IonContract Constructor

    @Override
    public IIonBuilder.SetIonDataSyntax set(ClientInfo clientInfo) {
        return null;
    }

    @Override
    public IIonBuilder.BuildSyntax<T> setIonData(T ionData) {
        return null;
    }

    @Override
    public IonRequestContract<T> build() {
        return null;
    }
}