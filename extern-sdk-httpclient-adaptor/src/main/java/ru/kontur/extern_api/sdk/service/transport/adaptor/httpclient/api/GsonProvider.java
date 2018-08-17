package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDescription;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Application;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534CuLetter;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Demand;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Ion;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Letter;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Report;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Submission;
import ru.kontur.extern_api.sdk.model.descriptions.StatReport;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.extras.DocflowDescriptionTypeAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonByteArrayAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonDateAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors.GsonRecipientAdaptor;

public class GsonProvider {

    public static Gson getGson() {
        return getPreConfiguredGsonBuilder().create();
    }

    public static GsonBuilder getPreConfiguredGsonBuilder() {
        DocflowDescriptionTypeAdapter typeAdapter = new DocflowDescriptionTypeAdapter()
                .registerDescription(DocflowDescription.class)
                .registerDescription(Fns534Letter.class)
                .registerDescription(Fns534Application.class)
                .registerDescription(Fns534CuLetter.class)
                .registerDescription(Fns534Demand.class)
                .registerDescription(Fns534Ion.class)
                .registerDescription(Fns534Report.class)
                .registerDescription(Fns534Submission.class)
                .registerDescription(StatReport.class);

        return new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                .registerTypeAdapter(Date.class, new GsonDateAdaptor())
                .registerTypeAdapter(byte[].class, new GsonByteArrayAdaptor())
                .registerTypeAdapter(Recipient.class, new GsonRecipientAdaptor())
                .registerTypeAdapter(Docflow.class, typeAdapter);
    }


}
