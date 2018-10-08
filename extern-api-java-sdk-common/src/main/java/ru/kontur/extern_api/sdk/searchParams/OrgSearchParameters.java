package ru.kontur.extern_api.sdk.it.utils;

import ru.kontur.extern_api.sdk.adaptor.QueryContext;

public class OrgSearchParameters<R> {

    private String inn;
    private String kpp;

    public OrgSearchParameters (String inn){

        this.inn = inn;
    }

    public OrgSearchParameters(String inn, String kpp){

        this.inn = inn;
        this.kpp = kpp;
    }

    public QueryContext<R> setSearchParams(QueryContext<R> context){

        return context.setInn(inn).setKpp(kpp);
    }
}
