/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
