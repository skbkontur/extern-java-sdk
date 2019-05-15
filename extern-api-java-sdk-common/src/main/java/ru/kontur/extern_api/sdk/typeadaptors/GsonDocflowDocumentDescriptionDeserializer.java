package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import ru.kontur.extern_api.sdk.model.DemandAttachmentRequisites;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowDocumentRequisites;
import ru.kontur.extern_api.sdk.model.DocumentType;

public class GsonDocflowDocumentDescriptionDeserializer
        implements JsonDeserializer<DocflowDocumentDescription> {

    private static final String REQUISITES = "requisites";
    private static final HashSet<String> FieldsToIgnore = new HashSet<>(
            Collections.singletonList(REQUISITES)
    );

    @Override
    public DocflowDocumentDescription deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context
    )
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        DocumentType documentType = GsonSerializationUtils
                .deserialize(obj, "type", DocumentType.class, context);

        DocflowDocumentDescription result = new DocflowDocumentDescription();

        GsonSerializationUtils.deserializeToObject(context, obj, result, FieldsToIgnore);

        Type requisitesType = documentType == DocumentType.Fns534DemandAttachment
                ? DemandAttachmentRequisites.class
                : DocflowDocumentRequisites.class;

        result.setRequisites(GsonSerializationUtils.deserialize(obj, REQUISITES, requisitesType, context));

        return result;
    }
}
