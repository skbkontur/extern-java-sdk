package ru.kontur.extern_api.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.typeadaptors.GsonByteArrayAdaptor;
import ru.kontur.extern_api.sdk.typeadaptors.GsonDateAdaptor;
import ru.kontur.extern_api.sdk.typeadaptors.GsonDocflowDeserializer;
import ru.kontur.extern_api.sdk.typeadaptors.GsonRecipientAdaptor;

public class GsonProvider {

    @NotNull
    public static Gson getGson() {
        return getPreConfiguredGsonBuilder().create();
    }

    @NotNull
    public static GsonBuilder getPreConfiguredGsonBuilder() {

        return new GsonBuilder()
                .disableHtmlEscaping()
                .setDateFormat(PublicDateFormat.FORMAT)
                .setFieldNamingPolicy(getFieldNamingPolicy())
                .registerTypeAdapter(byte[].class, new GsonByteArrayAdaptor())
                .registerTypeAdapter(Recipient.class, new GsonRecipientAdaptor())
                .registerTypeAdapter(Docflow.class, new GsonDocflowDeserializer());
    }

    @NotNull
    public static FieldNamingPolicy getFieldNamingPolicy() {
        return FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
    }
}
