package ru.kontur.extern_api.sdk.model;

import java.util.List;

public class CryptOperationResult {

    public CryptOperationStatus operationStatus;
    public List<FileStatus> fileStatuses;
    public String progress = operationStatus.toString();
}
