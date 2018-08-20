package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.extras;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDescription;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.Link;


public class DocflowDescriptionTypeAdapter implements JsonDeserializer<Docflow> {

    private final Map<String, Class<? extends DocflowDescription>> docflowDescriptionTypes
            = new HashMap<>();

    public DocflowDescriptionTypeAdapter registerDescription(
            Class<? extends DocflowDescription> description) {
        String classname = description.getSimpleName().toLowerCase();
        docflowDescriptionTypes.put(classname, description);
        return this;
    }

    @Override
    public Docflow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement type = jsonObject.get("type");

        String strType = type == null ? null : extractType(type);
        Type descriptionType = docflowDescriptionTypes.get(strType);
        if (descriptionType == null) {
            Logger.getLogger(String.valueOf(this.getClass()))
                    .warning(strType + " is not registered as DocflowDescription");
        }

        Docflow docflow = new Docflow();

        for (Field declaredField : Docflow.class.getDeclaredFields()) {
            deserializeDocflowField(context, jsonObject, descriptionType, docflow, declaredField);
        }

        return docflow;
    }

    private void deserializeDocflowField(
            JsonDeserializationContext context,
            JsonObject jsonObject,
            Type descriptionType,
            Docflow docflow,
            Field declaredField) {

        FieldNamingPolicy namingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES;

        declaredField.setAccessible(true);
        try {
            String fieldName = namingPolicy.translateName(declaredField);

            Object deserialized;
            JsonElement jsonField = jsonObject.get(fieldName);

            if (jsonField == null) {
                return;
            }

            switch (fieldName) {
                case "description":
                    deserialized = context.deserialize(jsonField, descriptionType);
                    break;
                case "documents":
                    deserialized = context.deserialize(jsonField, new TypeToken<List<Document>>() {
                    }.getType());
                    break;
                case "links":
                    deserialized = context.deserialize(jsonField, new TypeToken<List<Link>>() {
                    }.getType());
                    break;
                default:
                    deserialized = context.deserialize(jsonField, declaredField.getType());
            }

            declaredField.set(docflow, deserialized);
        } catch (IllegalAccessException ignored) {
            // declaredField.setAccessible(true) should work
        } finally {
            declaredField.setAccessible(false);
        }
    }

    private String extractType(JsonElement typeElement) {
        String s = typeElement.getAsString();
        String substring = s.substring(s.lastIndexOf(':') + 1);
        return substring.replaceAll("-", "").toLowerCase();
    }

}
