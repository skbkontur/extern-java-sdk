/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 * @author alexs
 */
public class AdditionalClientInfo {

    private SignerTypeEnum signerType = null;
    private String senderFullName = null;
    private Taxpayer taxpayer = null;

    public SignerTypeEnum getSignerType() {
        return signerType;
    }

    public void setSignerType(SignerTypeEnum signerType) {
        this.signerType = signerType;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public Taxpayer getTaxpayer() {
        return taxpayer;
    }

    public void setTaxpayer(Taxpayer taxpayer) {
        this.taxpayer = taxpayer;
    }

    public enum SignerTypeEnum {
        UNKNOWN("unknown"),

        CHIEF("chief"),

        REPRESENTATIVE("representative");

        private final String value;

        SignerTypeEnum(String value) {
            this.value = value;
        }

        public static SignerTypeEnum fromValue(String text) {
            for (SignerTypeEnum b : SignerTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
