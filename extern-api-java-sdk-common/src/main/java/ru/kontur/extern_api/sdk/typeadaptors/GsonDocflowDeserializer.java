package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Inventory;


public class GsonDocflowDeserializer extends GsonCustomObjectDeserializer {

    private static final String DESCRIPTION = "description";
    private static final String TYPE = "type";
    private static final HashSet<String> FieldsToIgnore = new HashSet<>(Arrays.asList(DESCRIPTION, TYPE));

    @Override
    public Docflow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        DocflowType dt = deserialize(obj, TYPE, DocflowType.class, context);

        Docflow docflow = new Docflow();

        if (dt == DocflowType.FNS534_INVENTORY) {
            docflow = new Inventory();
        }

        docflow.setType(deserialize(obj, TYPE, DocflowType.class, context));

        if (docflow.getType() == null) {
            docflow.setType(DocflowType.UNKNOWN);
        }

        Optional.ofNullable(docflow.getType())
                .map(DocflowType::getDescriptionType)
                .map(type -> deserialize(obj, DESCRIPTION, type, context))
                .ifPresent(docflow::setDescription);

        DeserializeToObject(context, obj, docflow, FieldsToIgnore);

        return docflow;
    }

}

