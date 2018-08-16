package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import ru.kontur.extern_api.sdk.model.Recipient;

public class GsonProvider {

    public static Gson getGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                .registerTypeAdapter(Date.class, new GsonDateAdaptor())
                .registerTypeAdapter(byte[].class, new GsonByteArrayAdaptor())
                .registerTypeAdapter(Recipient.class, new GsonRecipientAdaptor())
                .create();
    }

}
