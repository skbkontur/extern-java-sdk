package ru.kontur.extern_api.sdk.model.ion;

interface IIonBuilder {

    interface Syntax<T extends IonRequestData> extends
            SetSyntax<T>,
            SetIonDataSyntax<T>,
            SetIonPeriodSyntax<T>,
            BuildSyntax<T> {
    }

    interface SetSyntax<T extends IonRequestData> {
        SetIonDataSyntax<T> setClientInfo(ClientInfo clientInfo);
    }

    interface SetIonDataSyntax<T extends IonRequestData> {
        SetIonPeriodSyntax<T> setIonData(T ionData);
    }

    interface SetIonPeriodSyntax<T extends IonRequestData> {
        BuildSyntax<T> setIonPeriod(IonPeriod period);
    }

    interface BuildSyntax<T extends IonRequestData> {
        IonRequestContract<T> build();
    }
}

public class IonBuilder<T extends IonRequestData> implements IIonBuilder.Syntax<T> {

    private ClientInfo clientInfo;
    private T ionData;
    private IonPeriod period;

    private IonBuilder() {

    }

    public static <T extends IonRequestData> IIonBuilder.SetSyntax<T> Create() {
        return new IonBuilder<>();
    }

    public static IIonBuilder.SetSyntax<IonRequestData> CreateIonRequestData() {
        return new IonBuilder<>();
    }

    @Override
    public IIonBuilder.SetIonDataSyntax<T> setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        return this;
    }

    @Override
    public IIonBuilder.SetIonPeriodSyntax<T> setIonData(T ionData) {
        this.ionData = ionData;
        return this;
    }

    @Override
    public IonRequestContract<T> build() {
        return new IonRequestContract<>(clientInfo, period, ionData);
    }

    @Override
    public IIonBuilder.BuildSyntax<T> setIonPeriod(IonPeriod period) {
        this.period = period;
        return this;
    }
}

