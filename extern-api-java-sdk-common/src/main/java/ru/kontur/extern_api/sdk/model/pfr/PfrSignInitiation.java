package ru.kontur.extern_api.sdk.model.pfr;

import java.util.List;
import ru.kontur.extern_api.sdk.model.ConfirmType;
import ru.kontur.extern_api.sdk.model.Link;

public class PfrSignInitiation {

    private List<Link> links;
    private String requestId;
    private String taskId;
    private ConfirmType confirmType;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ConfirmType getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(ConfirmType confirmType) {
        this.confirmType = confirmType;
    }
}
