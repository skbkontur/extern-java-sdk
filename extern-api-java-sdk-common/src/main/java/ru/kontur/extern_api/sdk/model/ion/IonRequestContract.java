/*
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
 *
 */

package ru.kontur.extern_api.sdk.model.ion;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

public class IonRequestContract<TIonData extends IonRequestData> implements IonRequestContractInterface {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private @Nullable IonPeriod period;
    private ClientInfo additionalOrgInfo;
    private TIonData data;

    public IonRequestContract(
            @NotNull ClientInfo additionalOrgInfo,
            @Nullable IonPeriod period,
            @NotNull TIonData data) {
        this.period = period;
        this.additionalOrgInfo = additionalOrgInfo;
        this.data = data;
    }

    public ClientInfo getAdditionalOrgInfo() {
        return additionalOrgInfo;
    }

    public IonRequestData getData() {
        return data;
    }

    public @Nullable IonPeriod getPeriod() {
        return period;
    }

    public enum RequestType {
        WHOLE_ORGANIZATION(1),
        ALL_KPPS(2),
        ONE_KPP(3);

        private final int index;

        RequestType(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public enum AnswerFormat {
        RTF(1),
        XML(2),
        XLS(3),
        PDF(4);

        private final int index;

        AnswerFormat(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public enum ReportSelectionCondition {
        ALL_REPORT_TYPES(1),
        PRIMARY(2),
        CORRECTION(3);

        private final int index;

        ReportSelectionCondition(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public enum ReportGenerationCondition {
        GROUP_BY_ALL_PAYMENT_TYPES(1),
        NO_GROUP_BY_ALL_PAYMENT_TYPES(2);

        private final int index;

        ReportGenerationCondition(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }
}
