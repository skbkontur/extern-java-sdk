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

package ru.kontur.extern_api.sdk.httpclient;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DocflowsApi;
import ru.kontur.extern_api.sdk.model.DecryptInitiation;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.PrintDocumentData;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SendReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;


public class DocflowsAdaptorImpl extends BaseAdaptor implements DocflowsAdaptor {

    private final DocflowsApi api;

    DocflowsAdaptorImpl() {
        api = new DocflowsApi();
    }

    @Override
    public HttpClient getHttpClient() {
        return api.getHttpClient();
    }

    @Override
    public void setHttpClient(Supplier<HttpClient> httpClient) {
        super.httpClientSupplier = httpClient;
    }

    @Override
    public QueryContext<DocflowPage> getDocflows(QueryContext<?> cxt, DocflowFilter docflowFilter) {
        return setResultCarefully(cxt, transport -> transport.getDocflows(
                cxt.getAccountProvider().accountId().toString(),
                docflowFilter.getFinished(),
                docflowFilter.getIncoming(),
                docflowFilter.getSkip(),
                docflowFilter.getTake(),
                docflowFilter.getInnKpp(),
                docflowFilter.getUpdatedFrom(),
                docflowFilter.getUpdatedTo(),
                docflowFilter.getCreatedFrom(),
                docflowFilter.getCreatedTo(),
                docflowFilter.getOrgId(),
                docflowFilter.getTypeAsString(),
                docflowFilter.getOrderBy()
        ));
    }

    @Override
    public QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.lookupDocflow(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString())
        );
    }

    @Override
    public QueryContext<List<Document>> getDocuments(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getDocuments(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString()
        ));
    }

    @Override
    public QueryContext<Document> lookupDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.lookupDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    @Override
    public QueryContext<DocflowDocumentDescription> lookupDescription(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.lookupDescription(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    @Override
    public QueryContext<byte[]> getEncryptedContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getEncryptedContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    @Override
    public QueryContext<byte[]> getDecryptedContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getDecryptedContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    @Override
    public QueryContext<List<Signature>> getSignatures(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getSignatures(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    @Override
    public QueryContext<Signature> getSignature(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getSignature(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getSignatureId().toString()
        ));
    }

    @Override
    public QueryContext<byte[]> getSignatureContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getSignatureContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getSignatureId().toString()
        ));
    }

    @Override
    public QueryContext<String> print(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.print(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                new PrintDocumentData().content(cxt.getContentString())
        ));
    }

    @Override
    public QueryContext<ReplyDocument> generateReply(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.generateReplyDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getDocumentType(),
                cxt.getCertificate()
        ));
    }

    @Override
    public QueryContext<ReplyDocument> putReplyDocumentSignature(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.putReplyDocumentSignature(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getReplyId().toString(),
                cxt.getContent()
        ));
    }

    @Override
    public QueryContext<Docflow> sendReply(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.sendReply(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getReplyId().toString(),
                new SendReplyDocumentRequestData().senderIp(cxt.getUserIP())
        ));
    }

    @Override
    public QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getReplyDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getReplyId().toString()
        ));
    }

    @Override
    public QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.updateReplyDocumentContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getReplyId().toString(),
                cxt.getContent()
        ));
    }


    @Override
    public QueryContext<SignInitiation> cloudSignReplyDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.initCloudSignReplyDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getReplyId().toString(),
                false
        ));
    }

    @Override
    public QueryContext<SignConfirmResultData> confirmSignReplyDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.confirmCloudSignReplyDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDocflowId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getReplyId().toString(),
                cxt.getRequestId(),
                cxt.getSmsCode()
        ));
    }

    @Override
    public QueryContext<DecryptInitiation> cloudDecryptDocumentInit(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.decryptDocumentInCloud(
                cxt.getAccountProvider().accountId(),
                cxt.getDocflowId(),
                cxt.getDocumentId(),
                cxt.getCertificate()
        ));
    }

    @Override
    public QueryContext<byte[]> cloudDecryptDocumentConfirm(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.decryptConfirm(
                cxt.getAccountProvider().accountId(),
                cxt.getDocflowId(),
                cxt.getDocumentId(),
                cxt.getRequestId(),
                cxt.getSmsCode(),
                true
        ));
    }


    private <T> QueryContext<T> setResultCarefully
            (QueryContext<?> cxt, Function<DocflowsApi, ApiResponse<T>> tSupplier) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        try {
            ApiResponse<T> response = tSupplier.apply(transport(cxt));

            if (response.isSuccessful()) {
                return new QueryContext<T>(cxt, cxt.getEntityName())
                        .setResult(response.getData(), cxt.getEntityName());
            } else {
                return new QueryContext<T>(cxt, cxt.getEntityName())
                        .setServiceError(response.asApiException());
            }

        } catch (ApiException x) {
            return new QueryContext<T>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    private DocflowsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
