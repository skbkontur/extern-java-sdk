package ru.kontur.extern_api.sdk.portal.model;

import com.google.gson.annotations.SerializedName;

public class CertificateFields {

    @SerializedName("thumbprint")
    private String thumbprint;

    @SerializedName("raw_data")
    private byte[] rawData;

    public String getThumbprint() {
        return thumbprint;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public static class Names {

        private final String[] names;

        private Names(String... names) {
            this.names = names;
        }

        public static Names of(String... names) {
            return new Names(names);
        }

        @Override
        public String toString() {
            return String.join(",", names);
        }
    }
}
