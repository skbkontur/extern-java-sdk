package ru.kontur.extern_api.sdk.it.utils;

import java.util.Objects;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CertificateProvider;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.Stopwatch;


public class EmbeddedCertAuthProvider extends AuthenticationProviderAdaptor {

    private final AuthenticationProvider auth;

    public EmbeddedCertAuthProvider(ExternEngine engine) {
        Objects.requireNonNull(engine.getConfiguration().getThumbprint());

        engine.setCryptoProvider(
                Stopwatch.timeIt("MSCapi::new", CryptoProviderMSCapi::new)
        );
        CertificateProvider certificateProvider = thumbprint -> new QueryContext<byte[]>()
                .setResult(CertificateResource.read(thumbprint), QueryContext.CONTENT);

        auth = CertificateAuthenticationProvider
                .usingCertificate(certificateProvider)
                .setApiKeyProvider(engine.getApiKeyProvider())
                .setCryptoProvider(engine.getCryptoProvider())
                .setServiceBaseUriProvider(engine.getConfiguration()::getAuthBaseUri)
                .setSignatureKeyProvider(engine.getConfiguration()::getThumbprint)
                .buildAuthenticationProvider()
                .httpClient(engine.getHttpClient());
    }

    @Override
    public QueryContext<String> sessionId() {
        return auth.sessionId();
    }
}
