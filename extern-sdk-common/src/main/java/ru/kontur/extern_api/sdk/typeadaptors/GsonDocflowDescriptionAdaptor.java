package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class GsonDocflowDescriptionAdaptor implements JsonDeserializer<DocflowDescription> {

    @Override
    public DocflowDescription deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
