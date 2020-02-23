package ru.kontur.extern_api.sdk.model;

public enum CryptOperationErrorCode {
    None,
    Unknown,
    BadEncryptedData,
    EncryptedForOtherRecipient,
    UnknownCertificate
}
