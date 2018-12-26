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

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContractV1.AcceptType;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContractV1.Type;

/**
 * @see IonRequestContractV1#IonRequestContractV1(ClientInfo, Type, AcceptType, Date)
 */
public class IonRequestData {

    @SerializedName("ВидЗапр")
    private int type;

    @SerializedName("ФормОтв")
    private int formType;

    @SerializedName("НаДату")
    private String date;

    IonRequestData(int type, int acceptType, String onDate) {
        this.type = type;
        this.formType = acceptType;
        this.date = onDate;
    }

    public int getType() {
        return type;
    }

    public int getFormType() {
        return formType;
    }

    public String getDate() {
        return date;
    }
}
