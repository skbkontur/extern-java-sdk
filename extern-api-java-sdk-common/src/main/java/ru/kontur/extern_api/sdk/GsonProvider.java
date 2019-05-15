package ru.kontur.extern_api.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.Inventory;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.typeadaptors.GsonByteArrayAdaptor;
import ru.kontur.extern_api.sdk.typeadaptors.GsonDateAdaptor;
import ru.kontur.extern_api.sdk.typeadaptors.GsonDocflowDeserializer;
import ru.kontur.extern_api.sdk.typeadaptors.GsonDocflowDocumentDescriptionDeserializer;
import ru.kontur.extern_api.sdk.typeadaptors.GsonRecipientAdaptor;

public enum GsonProvider implements SerializationProvider {

    /**
     * Libapi compatible means that {@link FieldNamingPolicy#LOWER_CASE_WITH_DASHES} will be used.
     */
    LIBAPI() {
        @Override public Gson getGson() {
            return GsonProvider.getLibapiCompatibleGson();
        }
    },

    /**
     * Portal compatible means that {@link FieldNamingPolicy#UPPER_CAMEL_CASE} will be used.
     */
    PORTAL() {
        @Override public Gson getGson() {
            return GsonProvider.getPortalCompatibleGson();
        }
    },

    /**
     * Identity compatible means that {@link FieldNamingPolicy#IDENTITY} will be used.
     */
    IDENTITY() {
        @Override public Gson getGson() { return GsonProvider.getIdentityCompatibleGson(); }
    };

    @NotNull
    public static Gson getLibapiCompatibleGson() {
        return getPreConfiguredGsonBuilder().create();
    }

    @NotNull
    public static GsonBuilder getPreConfiguredGsonBuilder() {

        return new GsonBuilder()
                .disableHtmlEscaping()
                .setDateFormat(PublicDateFormat.FORMAT)
                .setFieldNamingPolicy(getFieldNamingPolicy())
                .registerTypeAdapter(byte[].class, new GsonByteArrayAdaptor())
                .registerTypeAdapter(Date.class, new GsonDateAdaptor())
                .registerTypeAdapter(Recipient.class, new GsonRecipientAdaptor())
                .registerTypeAdapter(Docflow.class, new GsonDocflowDeserializer<>(Docflow::new))
                .registerTypeAdapter(Inventory.class, new GsonDocflowDeserializer<>(Inventory::new))
                .registerTypeAdapter(DocflowDocumentDescription.class, new GsonDocflowDocumentDescriptionDeserializer());
    }

    @NotNull
    public static FieldNamingPolicy getFieldNamingPolicy() {
        return FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
    }

    @NotNull
    public static Gson getPortalCompatibleGson() {
        return getPreConfiguredGsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
    }

    @NotNull
    public static Gson getIdentityCompatibleGson() {
        return getPreConfiguredGsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
    }

}
