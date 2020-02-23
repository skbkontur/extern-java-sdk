package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

public class FileStatus {
    public UUID fileId;
    public FileTaskStatus status;
    public UUID errorId;
    public CryptOperationErrorCode errorCode;
    public long resultSize;
    public String resultMD5;
    public UUID resultId;
}
