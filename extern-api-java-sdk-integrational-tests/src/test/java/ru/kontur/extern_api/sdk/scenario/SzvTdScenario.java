package ru.kontur.extern_api.sdk.scenario;

import org.bouncycastle.crypto.signers.ECGOST3410_2012Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMetaRequest;
import ru.kontur.extern_api.sdk.model.pfr.Zped.ZpedRequestContract;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.ContentService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.*;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

@Execution(ExecutionMode.CONCURRENT)
public class SzvTdScenario {
    private static ExternEngine engine;
    private static TestSuite test;
    private static CryptoUtils cryptoUtils;
    private static HttpClient httpClient;

    @BeforeAll
    static void setUpClass() {
        test = TestSuite.Load();
        engine = test.engine;
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        httpClient = engine.getHttpClient();
    }

    @Test
    void main() throws Exception {
        try {
            scenario();
        } catch (ApiException e) {
            System.err.println(e.prettyPrint());
            throw e;
        }
    }

    private void scenario() throws Exception {
        String thumbprint = engine.getConfiguration().getThumbprint();
        String cert = cryptoUtils.loadX509(thumbprint);
        DraftMetaRequest draftMetaRequest = Arrays
                .stream(TestUtils.getTestData(cert))
                .filter(td -> td.getClientInfo().getRecipient().getUpfrCode() != null)
                .findFirst()
                .map(TestUtils::toDraftMetaRequest).orElseThrow(() -> new RuntimeException("no suitable data for zped tests"));


        DraftService draftService = engine.getDraftService();
        UUID draftId = draftService.createAsync(draftMetaRequest).join().getOrThrow().getId();

        ZpedRequestContract zpedRequestContract = loadZped("/docs/pfr/zped/zpedUl.json");
        DraftDocument draftDocument = draftService.createAndBuildZpedAsync(draftId, zpedRequestContract)
                .join()
                .getOrThrow();

        draftService.checkAsync(draftId).join().getOrThrow();

        draftDocument = draftService.lookupDocumentAsync(draftId, draftDocument.getId()).join().getOrThrow();
        String contentLink = draftDocument.getDecryptedContentLink().getHref();
        String data = httpClient.followGetLink(contentLink, String.class);
        Assertions.assertTrue(data.contains("ЗПЭД"));

        UUID dataToSignContentId = draftDocument.getDataToSignContentId();

        ContentService contentService = engine.getContentService();
        byte[] dataToSign = contentService.getContent(dataToSignContentId).join();
        Assertions.assertNotNull(dataToSign);
        Assertions.assertEquals(64, dataToSign.length);
    }

    private static ZpedRequestContract loadZped(String path) {
        return Resources.loadFromJson(path, ZpedRequestContract.class);
    }
}
