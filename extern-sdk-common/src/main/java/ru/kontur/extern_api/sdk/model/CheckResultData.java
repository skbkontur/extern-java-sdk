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

package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author AlexS
 */
public class CheckResultData {

    private Map<String, List<CheckError>> documentsErrors = null;
    private List<CheckError> commonErrors = null;

    public CheckResultData documentsErrors(Map<String, List<CheckError>> documentsErrors) {
        this.documentsErrors = documentsErrors;
        return this;
    }

    /**
     * Get documentsErrors
     *
     * @return documentsErrors
     */
    public Map<String, List<CheckError>> getDocumentsErrors() {
        return documentsErrors;
    }

    public void setDocumentsErrors(Map<String, List<CheckError>> documentsErrors) {
        this.documentsErrors = documentsErrors;
    }

    public CheckResultData commonErrors(List<CheckError> commonErrors) {
        this.commonErrors = commonErrors;
        return this;
    }

    public CheckResultData addCommonErrorsItem(CheckError commonErrorsItem) {
        if (this.commonErrors == null) {
            this.commonErrors = new ArrayList<>();
        }
        this.commonErrors.add(commonErrorsItem);
        return this;
    }

    /**
     * Get commonErrors
     *
     * @return commonErrors
     */
    public List<CheckError> getCommonErrors() {
        return commonErrors;
    }

    public void setCommonErrors(List<CheckError> commonErrors) {
        this.commonErrors = commonErrors;
    }
}
