package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonByteArrayAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonDateAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonDocflowDeserializer;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonRecipientAdaptor;

public class GsonProvider {

    public static Gson getGson() {
        return getPreConfiguredGsonBuilder().create();
    }

    public static GsonBuilder getPreConfiguredGsonBuilder() {

        return new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                .registerTypeAdapter(Date.class, new GsonDateAdaptor())
                .registerTypeAdapter(byte[].class, new GsonByteArrayAdaptor())
                .registerTypeAdapter(Recipient.class, new GsonRecipientAdaptor())
                .registerTypeAdapter(Docflow.class, new GsonDocflowDeserializer());
    }


}
