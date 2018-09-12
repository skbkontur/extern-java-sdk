package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public interface Urn<T extends Enum<?>> {

    default String getRepresentation() {
        try {
            return this.getClass()
                    .getField(((Enum) this).name())
                    .getAnnotation(SerializedName.class)
                    .value();

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    default String getName() {
        String representation = getRepresentation();
        int i = representation.lastIndexOf(':');
        if (i >= 0) {
            return representation.substring(i + 1);
        } else {
            return representation;
        }
    }
}
