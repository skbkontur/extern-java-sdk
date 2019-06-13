package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Cостояние документооборота.
 */
public enum DocflowStateTypes implements Urn<DocflowStateTypes> {

    /**
     * Cостояние документооборота:
     * Состояние не определенно.
     */
    @SerializedName("urn:docflow-state:neutral")
    NEUTRAL,

    /**
     * Cостояние документооборота:
     * Успешно обработан.
     */
    @SerializedName("urn:docflow-state:successful")
    SUCCESSFUL,

    /**
     * Cостояние документооборота:
     * Обработка завершилась ошибкой\отказом.
     */
    @SerializedName("urn:docflow-state:failed")
    FAILED,

    /**
     * Cостояние документооборота:
     * Обработка в целом завершилась успешно, но у контролирующего органа есть
     * претензии. Возможно потребуется отправка корректировки.
     */
    @SerializedName("urn:docflow-state:warning")
    WARNING
}