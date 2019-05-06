package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;

public abstract class GsonCustomObjectDeserializer implements JsonDeserializer {

    protected void DeserializeToObject(
            JsonDeserializationContext context,
            JsonObject obj,
            Object result,
            Set<String> ignoreFields
    ) {
        FieldNamingPolicy namingPolicy = GsonProvider.getFieldNamingPolicy();
        for (Field field : result.getClass().getDeclaredFields()) {
            String fieldName = namingPolicy.translateName(field);
            if(ignoreFields.contains(fieldName)){
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
    }

    protected static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Class<T> type,
            JsonDeserializationContext cxt) {
        return deserialize(obj, fieldName, (Type) type, cxt);
    }

    protected static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Type type,
            JsonDeserializationContext cxt) {
        return cxt.deserialize(obj.get(fieldName), type);
    }

}
