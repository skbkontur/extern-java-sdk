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
import ru.kontur.extern_api.sdk.model.BuildDocumentType;

import java.text.SimpleDateFormat;

public class IonRequestContract<TIonData extends IonRequestData> implements IonRequestContractInterface {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private int version;
    private @Nullable IonPeriod period;
    private ClientInfo additionalOrgInfo;
    private TIonData data;

    public IonRequestContract(
            @NotNull ClientInfo additionalOrgInfo,
            @Nullable IonPeriod period,
            @NotNull TIonData data) {
        this.period = period;
        this.version = 1;
        this.additionalOrgInfo = additionalOrgInfo;
        this.data = data;
    }

    public int getVersion() {
        return version;
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
        WHOLE_ORGANIZATION,
        ALL_KPPS,
        ONE_KPP
    }

    public enum AnswerFormat {
        RTF,
        XML,
        XLS,
        PDF
    }

    public enum ReportSelectionCondition {
        ALL_REPORT_TYPES,
        PRIMARY,
        CORRECTION
    }

    public enum ReportGenerationCondition {
        GROUP_BY_ALL_PAYMENT_TYPES,
        NO_GROUP_BY_ALL_PAYMENT_TYPES
    }
}
