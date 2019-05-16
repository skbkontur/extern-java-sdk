package ru.kontur.extern_api.sdk.model.ion;

import java.time.Year;

public class IonPeriod {
    private int year;

    public IonPeriod(Year year) {
        this.year = year.getValue();
    }

    public int getYear() {
        return year;
    }
}
