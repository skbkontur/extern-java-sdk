package ru.kontur.extern_api.sdk.model;

import java.util.List;

public class TaskPage {

    private int skip;

    private int take;

    private int totalCount;

    private List<TaskStatus> apiTaskPageItems;

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TaskStatus> getApiTaskPageItems() {
        return apiTaskPageItems;
    }

    public void setApiTaskPageItems(List<TaskStatus> apiTaskPageItems) {
        this.apiTaskPageItems = apiTaskPageItems;
    }
}
