package ru.kontur.extern_api.sdk.model;

import java.util.Optional;

public interface IDocflowDescription {

    default <T extends IDocflowDescription> Optional<T> as(Class<T> clazz) {
        return clazz.isInstance(this)
                ? Optional.of(clazz.cast(this))
                : Optional.empty();
    }

}
