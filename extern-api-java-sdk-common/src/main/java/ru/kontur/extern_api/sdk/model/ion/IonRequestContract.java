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

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.BuildDocumentContract;

public class IonRequestContract implements BuildDocumentContract {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private int version;
    private ClientInfo additionalOrgInfo;
    private IonRequestData data;

    public IonRequestContract(
            @NotNull ClientInfo additionalOrgInfo,
            @NotNull Type type,
            @NotNull AcceptType acceptType,
            @NotNull Date onDate
    ) {
        this.version = 1;
        this.additionalOrgInfo = additionalOrgInfo;

        int iType = type.ordinal() + 1;
        int iAcceptType = acceptType.ordinal() + 1;
        String sDate = formatter.format(onDate);

        this.data = new IonRequestData(iType, iAcceptType, sDate);
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

    public enum Type {
        WHOLE_ORGANIZATION,
        ALL_KPPS,
        ONE_KPP
    }

    public enum AcceptType {
        RTF,
        XML,
        XLS,
        PDF
    }
}
