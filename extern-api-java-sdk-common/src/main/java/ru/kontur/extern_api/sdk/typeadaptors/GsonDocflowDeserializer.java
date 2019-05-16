package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;
import javax.print.Doc;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Inventory;


public class GsonDocflowDeserializer<T extends Docflow> implements JsonDeserializer<T> {

    private static final String DESCRIPTION = "description";
    private static final String TYPE = "type";
    private static final HashSet<String> FieldsToIgnore = new HashSet<>(Arrays.asList(DESCRIPTION, TYPE));
    private final Supplier<T> tConstructor;

    public GsonDocflowDeserializer(Supplier<T> tConstructor) {
        this.tConstructor = tConstructor;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        DocflowType dt = GsonSerializationUtils.deserialize(obj, TYPE, DocflowType.class, context);

        Docflow docflow = tConstructor.get();
        if (dt == DocflowType.FNS534_INVENTORY) {
            docflow = new Inventory();
        }

        docflow.setType(GsonSerializationUtils.deserialize(obj, TYPE, DocflowType.class, context));

        if (docflow.getType() == null) {
            docflow.setType(DocflowType.UNKNOWN);
        }

        Optional.ofNullable(docflow.getType())
                .map(DocflowType::getDescriptionType)
                .map(type -> GsonSerializationUtils.deserialize(obj, DESCRIPTION, type, context))
                .ifPresent(docflow::setDescription);

        GsonSerializationUtils.deserializeToObject(context, obj, docflow, FieldsToIgnore);

        return upcast(docflow);
    }

    @SuppressWarnings("unchecked")
    private T upcast(Docflow u) {
        return (T) u;
    }

}

