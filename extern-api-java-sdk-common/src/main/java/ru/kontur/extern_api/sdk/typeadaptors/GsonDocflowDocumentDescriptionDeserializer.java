package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import ru.kontur.extern_api.sdk.model.DemandAttachmentRequisites;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowDocumentRequisites;
import ru.kontur.extern_api.sdk.model.DocumentType;

public class GsonDocflowDocumentDescriptionDeserializer extends GsonCustomObjectDeserializer {

    private static final String REQUISITES = "requisites";
    private static final HashSet<String> FieldsToIgnore = new HashSet<>(Arrays.asList(REQUISITES));

    @Override
    public DocflowDocumentDescription deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context
    )
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        DocumentType documentType = deserialize(obj, "type", DocumentType.class, context);

        DocflowDocumentDescription result = new DocflowDocumentDescription();

        DeserializeToObject(context, obj, result, FieldsToIgnore);

        if (documentType == DocumentType.Fns534DemandAttachment) {
            result.setRequisites(deserialize(obj, REQUISITES, DemandAttachmentRequisites.class, context));
        } else {
            result.setRequisites(deserialize(obj, REQUISITES, DocflowDocumentRequisites.class, context));
        }

        return result;
    }
}