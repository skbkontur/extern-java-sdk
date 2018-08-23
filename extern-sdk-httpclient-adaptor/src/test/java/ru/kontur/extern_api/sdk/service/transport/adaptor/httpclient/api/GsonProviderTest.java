package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Demand;

public class GsonProviderTest {

    @Test
    public void gsonShouldParseDocflowDescription() {
        Gson gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();

        String docflow = "{\n"
                + "  \"id\": \"f3201aef-dea5-43ba-8a12-d332856299c0\",\n"
                + "  \"type\": \"urn:docflow:fns534-demand\",\n"
                + "  \"status\": \"urn:docflow-common-status:delivered\",\n"
                + "  \"description\": {\n"
                + "    \"cu\": \"0087\",\n"
                + "    \"transit-сu\": \"0087\",\n"
                + "    \"attachments-count\": 1, \n"
                + "    \"form-versions\": [\n"
                + "         { \"knd\": \"exists\" } \n"
                + "     ]\n"
                + "  }"
                + "}";

        Docflow df = gson.fromJson(docflow, Docflow.class);
        System.out.println(docflow);
        System.out.println(gson.toJson(df));

        Assert.assertEquals(DocflowStatus.DELIVERED, df.getStatus());
        Assert.assertEquals(DocflowType.FNS_534_DEMAND, df.getType());
        Assert.assertEquals(Fns534Demand.class, df.getDescription().getClass());

        Fns534Demand description = (Fns534Demand) df.getDescription();
        Assert.assertEquals("0087", description.getCu());
        Assert.assertEquals(1, description.getAttachmentsCount());
        Assert.assertEquals("exists", description.getFormVersions().get(0).getKnd());
    }
}
