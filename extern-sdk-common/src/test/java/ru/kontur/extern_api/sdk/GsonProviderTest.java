package ru.kontur.extern_api.sdk;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.TransactionTypes.Fns534Report;
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

    @Test
    public void gsonShouldReadUrnCorrectly() {
        Gson gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();

        Fns534Report report = Fns534Report.Report;
        String sReport = gson.toJson(report);
        Fns534Report dReport = gson.fromJson(sReport, Fns534Report.class);

        Assertions.assertEquals(report, dReport);

    }

    @Test
    public void gsonShouldSerializeUrnCorrectly() {
        Gson gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();

        String report = String.format("\"%s\"", Fns534Report.Report.getRepresentation());

        Fns534Report dReport = gson.fromJson(report, Fns534Report.class);
        String sReport = gson.toJson(dReport);

        Assertions.assertEquals(report, sReport);

    }

    @Test
    public void gsonShouldReadUrnCorrectlyInObject() {
        Gson gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();
        Doc doc = new Doc();
        doc.setType(Fns534Report.Report);
        String jDoc = gson.toJson(doc);
        Doc doc1 = gson.fromJson(jDoc, Doc.class);
        Assertions.assertEquals(doc.getType(), doc1.getType());

    }

    @Test
    public void gsonShouldSerializeUrnCorrectlyInObject() {
        Gson gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();

        String report = Fns534Report.Report.getRepresentation();
        Doc doc = new Doc();
        doc.setType(Fns534Report.Report);

        String jDoc = String.format("{ \"type\": \"%s\"}", report);
        Doc dReport = gson.fromJson(jDoc, Doc.class);

        Assertions.assertEquals(Fns534Report.Report, dReport.getType());
    }


    public static class Doc {

        private Fns534Report type;

        public Fns534Report getType() {
            return type;
        }

        public void setType(Fns534Report type) {
            this.type = type;
        }
    }
}
