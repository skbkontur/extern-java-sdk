package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.GsonProvider;

class GsonSerializationUtils {

    static void deserializeToObject(
            JsonDeserializationContext context,
            JsonObject obj,
            Object result,
            Set<String> ignoreFields
    ) {
        FieldNamingPolicy namingPolicy = GsonProvider.getFieldNamingPolicy();

        Stream<Field> fields = getAllFields(result.getClass());

        for (Field field : fields.collect(Collectors.toList())) {
            String fieldName = namingPolicy.translateName(field);
            if (ignoreFields.contains(fieldName)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object deserialized = deserialize(obj, fieldName, field.getGenericType(), context);
                if (deserialized != null)
                    field.set(result, deserialized);
            } catch (IllegalAccessException ignored) {
                // in field#setAccessible we trust
            } finally {
                field.setAccessible(false);
            }
        }
    }

    @NotNull
    private static Stream<Field> getAllFields(Class<?> aClass) {
        Stream<Field> fields = Arrays.stream(aClass.getDeclaredFields());
        while (true) {
            Class<?> superclass = aClass.getSuperclass();
            if (superclass == null) {
                break;
            }

            fields = Stream.concat(fields, Arrays.stream(superclass.getDeclaredFields()));
            aClass = superclass;

        }
        return fields;
    }

    static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Class<T> type,
            JsonDeserializationContext cxt
    ) {
        return deserialize(obj, fieldName, (Type) type, cxt);
    }

    static <T> T deserialize(
            JsonObject obj,
            String fieldName,
            Type type,
            JsonDeserializationContext cxt
    ) {
        return cxt.deserialize(obj.get(fieldName), type);
    }

}
