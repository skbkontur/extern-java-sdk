package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.model.DemandAttachmentRequisites;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowDocumentRequisites;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentType;
import ru.kontur.extern_api.sdk.model.Inventory;

public class GsonDocflowDocumentDescriptionDeserializer implements JsonDeserializer {

    @Override
    public DocflowDocumentDescription deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        DocumentType documentType = deserialize(obj, "type", DocumentType.class, context);

        DocflowDocumentDescription result = new DocflowDocumentDescription();

        FieldNamingPolicy namingPolicy = GsonProvider.getFieldNamingPolicy();

        for (Field field : DocflowDocumentDescription.class.getDeclaredFields()) {
            String fieldName = namingPolicy.translateName(field);
            if (Objects.equals(fieldName, "requisites")) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object deserialized = deserialize(obj, fieldName, field.getGenericType(), context);
                field.set(result, deserialized);
            } catch (IllegalAccessException ignored) {

            } finally {
                field.setAccessible(false);
            }
        }

        if(documentType == DocumentType.Fns534DemandAttachment){
            result.setRequisites(deserialize(obj, "requisites", DemandAttachmentRequisites.class, context));
        }else {
            result.setRequisites(deserialize(obj, "requisites", DocflowDocumentRequisites.class, context));
        }

        return result;
    }

    private static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Class<T> type,
            JsonDeserializationContext cxt) {
        return deserialize(obj, fieldName, (Type) type, cxt);
    }

    private static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Type type,
            JsonDeserializationContext cxt) {
        return cxt.deserialize(obj.get(fieldName), type);
    }

}
