package ru.kontur.extern_api.sdk.model;

public enum CryptOperationStatus {
    Enqueued,
    InProgress,
    Completed,
    CanceledByUser,
    Timeout,
    Crashed,
    UserHasUnconfirmedOperation,
    AwaitingForConfirmation,
    ExceededConfirmationAttemptsCount
}
