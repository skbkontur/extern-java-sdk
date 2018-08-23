package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDescription;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.DocflowType;


public class DocflowDeserializer implements JsonDeserializer<Docflow> {

    private final Map<String, Class<? extends DocflowDescription>> docflowDescriptionTypes
            = new HashMap<>();

    public DocflowDeserializer registerDescription(
            Class<? extends DocflowDescription> description) {
        String classname = description.getSimpleName().toLowerCase();
        docflowDescriptionTypes.put(classname, description);
        return this;
    }

    private TypeToken<List<Document>> listDocToken = new TypeToken<List<Document>>() {

    };

    private TypeToken<List<Link>> listLinkToken = new TypeToken<List<Link>>() {

    };

    @Override
    public Docflow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        Docflow df = new Docflow();
        df.setType(deserialize(obj, "type", DocflowType.class, context));
        if (df.getType() != null)
            df.setDescription(deserialize(obj, "description", df.getType().getType(), context));
        df.setId(deserialize(obj, "id", UUID.class, context));
        df.setStatus(deserialize(obj, "status", DocflowStatus.class, context));
        df.setLastChangeDate(deserialize(obj, "last-change-date", Date.class, context));
        df.setSendDate(deserialize(obj, "send-date", Date.class, context));
        df.setDocuments(deserialize(obj, "documents", listDocToken.getType(), context));
        df.setLinks(deserialize(obj, "links", listLinkToken.getType(), context));

        return df;
    }

    private static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Type type,
            JsonDeserializationContext cxt) {
        return cxt.deserialize(obj.get(fieldName), type);
    }

}
