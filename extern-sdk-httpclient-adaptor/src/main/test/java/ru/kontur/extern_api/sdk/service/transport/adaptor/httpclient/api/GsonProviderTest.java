package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Letter;

class GsonProviderTest {

    @Test
    void gsonShouldParseDocflowDescription() {
        Gson gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();

        String docflow = "{\n"
                + "  \"id\": \"f3201aef-dea5-43ba-8a12-d332856299c0\",\n"
                + "  \"type\": \"urn:docflow:fns534-letter\",\n"
                + "  \"status\": \"urn:docflow-common-status:delivered\",\n"
                + "  \"description\": {\n"
                + "    \"cu\": \"0087\",\n"
                + "    \"recipient\": \"0087\",\n"
                + "    \"subject\": \"454545\",\n"
                + "    \"origin-docflow-id\": \"00000000-0000-0000-0000-000000000000\"\n"
                + "  }"
                + "}";

        Docflow df = gson.fromJson(docflow, Docflow.class);

        assertEquals(Fns534Letter.class, df.getDescription().getClass());
        assertEquals("0087", ((Fns534Letter) df.getDescription()).getCu());

        System.out.println(gson.toJson(df));
    }
}
