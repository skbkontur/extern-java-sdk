package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.Link;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class GsonDocflowDeserializer implements JsonDeserializer<Docflow> {

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
        Optional.ofNullable(df.getType())
                .map(type -> deserialize(obj, "description", type.getDescriptionType(), context))
                .ifPresent(df::setDescription);

        FieldNamingPolicy namingPolicy = GsonProvider.getFieldNamingPolicy();

        for (Field field : Docflow.class.getDeclaredFields()) {
            String fieldName = namingPolicy.translateName(field);
            if (Objects.equals(fieldName, "type") || Objects.equals(fieldName, "description")) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object deserialized = deserialize(obj, fieldName, field.getGenericType(), context);
                field.set(df, deserialized);
            }
            catch (IllegalAccessException ignored) {
                // field.setAccessible(true) should work
            }
            finally {
                field.setAccessible(false);
            }
        }


        return df;
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
